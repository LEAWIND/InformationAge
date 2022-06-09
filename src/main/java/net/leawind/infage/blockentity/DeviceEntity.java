package net.leawind.infage.blockentity;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.script.CompiledScript;
import net.leawind.infage.Infage;
import net.leawind.infage.script.CompileStatus;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.infage.script.mtt.CompileTask;
import net.leawind.infage.script.mtt.ExecuteTask;
import net.leawind.infage.util.DataEncoding;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;


public abstract class DeviceEntity extends BlockEntity implements Tickable {

	public int tickCounter = 0; // tick 游戏刻 计数器
	public boolean isRunning = false; // 是否已开机
	public String consoleOutputs = ""; // 输出缓冲区

	public int storageSize = 128; // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )
	public byte[] storage; // 磁盘内容

	public int portsCount = 1; // 接口数量 (最多同时有多少个设备和它相连接)
	public byte portsStatus[]; // 各接口的 目标接口的 id, 若未连接则为 -1
	public long portsX[]; // 各接口所连接方块的坐标(不一定正确，因为对方可能被破坏)
	public long portsY[]; // 如果不正确，就会在 World.tickBlockEntities 的时候被发现，然后立即断开这个连接
	public long portsZ[];

	public String receiveCaches[]; // 各接口的接收缓存
	public String sendCaches[]; // 各接口的发送缓存
	public String script_tick = ";"; // tick 脚本
	public String prev_script_tick = ""; // 之前的 tick 脚本
	public CompiledScript compiledScript_tick = null; // 编译后的 tick 脚本
	public boolean isCompiling = false; // 是否正在编译
	public CompileStatus compileStatus = CompileStatus.UNKNOWN; // 编译状态

	// 初始化
	public void init() {
		this.storage = new byte[this.storageSize];

		this.portsX = new long[this.portsCount];
		this.portsY = new long[this.portsCount];
		this.portsZ = new long[this.portsCount];

		this.portsStatus = new byte[this.portsCount];
		Arrays.fill(this.portsStatus, (byte) -1);

		this.sendCaches = new String[this.portsCount];
		Arrays.fill(this.sendCaches, "");

		this.receiveCaches = new String[this.portsCount];
		Arrays.fill(this.receiveCaches, "");
	}

	public DeviceEntity(BlockEntityType<?> type) {
		super(type);
		init();
	}

	/*
	 * 要实现在方块实体中储存数据，就要能够加载和保存数据。 在 1.16.5 中是 toTag() 和 fromTag() 1.16.5 参考
	 * [https://fabricmc.net/wiki/tutorial:blockentity?rev=1563817083]
	 * 
	 * 在 1.18 中标识符变成了 writeNbt() 和 readNbt() 1.18 参考 [https://fabricmc.net/wiki/tutorial:blockentity]
	 */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		// 父类的 toTag() 会将方块的 id 和坐标 放到 tag 中。
		// 如果没有这个步骤，以后就无法通过坐标查找到这个方块实体，这些数据就相当于丢失了。
		tag = super.toTag(tag);

		tag.putInt("tickCounter", this.tickCounter);
		tag.putBoolean("isRunning", this.isRunning);
		tag.putInt("storageSize", this.storageSize);
		tag.putByteArray("storage", this.storage);
		tag.putString("consoleOutputs", this.consoleOutputs);

		tag.putInt("portsCount", this.portsCount);
		tag.putByteArray("portsStatus", this.portsStatus);
		tag.putLongArray("portsX", this.portsX);
		tag.putLongArray("portsY", this.portsY);
		tag.putLongArray("portsZ", this.portsZ);
		tag.putString("script_tick", this.script_tick);

		tag.putString("sendCaches", DataEncoding.encode(this.sendCaches));
		tag.putString("receiveCaches", DataEncoding.encode(this.receiveCaches));

		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));

		this.tickCounter = tag.getInt("tickCounter");
		this.isRunning = tag.getBoolean("isRunning");
		this.storageSize = tag.getInt("storageSize");
		this.storage = tag.getByteArray("storage");
		this.consoleOutputs = tag.getString("consoleOutputs");

		this.portsCount = tag.getInt("portsCount");
		this.portsStatus = tag.getByteArray("portsStatus");
		this.portsX = tag.getLongArray("portsX");
		this.portsY = tag.getLongArray("portsY");
		this.portsZ = tag.getLongArray("portsZ");
		this.script_tick = tag.getString("script_tick"); // tick 脚本

		this.sendCaches = DataEncoding.decode(tag.getString("sendCaches"));
		this.receiveCaches = DataEncoding.decode(tag.getString("receiveCaches"));

		// 每次读取都重置编译状态
		this.compileStatus = CompileStatus.UNKNOWN;
	}

	@Override
	public void tick() {
		this.tickCounter++;
		if (this.isRunning) // 如果设备在运行
			this.deviceTick(); // 执行设备刻
	}

	// 获取指定接口所连接的设备方块实体
	public DeviceEntity getConnectedDevice(int portId) {
		if (this.portsStatus[portId] < 0)
			return null;

		BlockPos pos = new BlockPos(this.portsX[portId], this.portsY[portId], this.portsZ[portId]);
		BlockEntity blockEntity = this.getWorld().getBlockEntity(pos);

		if (blockEntity instanceof DeviceEntity)
			return (DeviceEntity) blockEntity;
		else
			return null;
	}

	// 设置脚本
	public synchronized void setScirpt_tick(String str) {
		this.script_tick = str;
		this.compileStatus = CompileStatus.UNKNOWN;
	}

	// 设置本地存储
	public synchronized void setStorage(byte[] storage) {
		if (storage == null) {
			Arrays.fill(this.storage, (byte) 0);
		} else {
			if (storage.length < this.storageSize)
				Arrays.fill(this.storage, storage.length, this.storageSize, (byte) 0);
			System.arraycopy(storage, 0, this.storage, 0, this.storageSize);
		}
	}

	// 设置本地存储
	public synchronized void setStorage(String str) throws UnsupportedEncodingException {
		this.setStorage(str.getBytes("UTF-8"));
	}

	// 向控制台写入新的字符串
	public synchronized void writeOutputs(String str) {
		this.consoleOutputs += str;
		int n = this.consoleOutputs.length() - Infage.OUTPUTS_SIZE;
		if (n > 0)
			this.consoleOutputs = this.consoleOutputs.substring(n);
	}

	// 向控制台添加记录，包含 记录者名称 和 时间（tick数）信息
	public synchronized void writeLog(String name, String str) {
		this.writeOutputs("[" + this.tickCounter + "] (" + name + ") " + str);
	}

	// 清空控制台
	public synchronized void clearOutputs() {
		this.consoleOutputs = "";
	}

	// 设置输出缓存
	public synchronized void setSendCaches(String[] sc) {
		if (sc == null)
			return;
		int i;
		for (i = 0; i < this.portsCount && i < sc.length; i++)
			this.sendCaches[i] = sc[i].length() > Infage.MAX_TRANSMISSION_UNIT ? //
					sc[i].substring(0, Infage.MAX_TRANSMISSION_UNIT) : //
					sc[i];
		for (; i < this.portsCount; i++)
			this.sendCaches[i] = null;

	}

	// 清空输出缓存
	public synchronized void clearSendCaches() {
		Arrays.fill(this.sendCaches, null);
	}

	// 获取当前设备实体的 DeviceObj 实例，用于 定义在脚本中可以使用的 可调用方法 和 可读写属性
	public DeviceObj getDeviceObj() {
		return new DeviceObj(this);
	}

	// 设备刻
	public void deviceTick() {
		// System.out.println("Device ticking: " + this.getClass());
		switch (this.compileStatus) {
			case UNKNOWN: // 还没编译
				this.compileStatus = CompileStatus.DISTRIBUTED;
				// 布置任务:编译
				CompileTask task = new CompileTask(this);
				ScriptHelper.MTM_COMPILE.addTask(task);
				break;
			case DISTRIBUTED: // 已经布置任务,暂未完成
				break;
			case SUCCESS: // 编译已完成, 创建新任务
				// 获取当前设备实体的 DeviceObj 实例，用于在脚本中提供 可调用方法 和 可读写属性
				ExecuteTask executeTask = new ExecuteTask(this);
				executeTask.deviceObj = this.getDeviceObj();
				// 创建绑定，并 设置 标识符 与 DeviceObj实例 的对应关系
				executeTask.bindings = ScriptHelper.ENGINE.createBindings();
				executeTask.bindings.put("Device", executeTask.deviceObj); // 在脚本中可以通过 Device 访问这个对象
				// 布置任务:执行
				ScriptHelper.MTM_EXEC.addTask(executeTask);
				break;
			case ERROR: // 编译出错
			default:
				break;
		}
	}

	// 开机
	public void device_boot() {
		this.isRunning = true;
		this.tickCounter = 0;
	}

	// 关机
	public void device_shutdown() {
		this.isRunning = false;
		// 断开所有连接
		Arrays.fill(this.portsStatus, (byte) -1);
	}

	// 切换开关机状态
	public void togglePower() {
		this.isRunning = !this.isRunning;
	}

	// 重启
	public void device_reboot() {
		this.device_shutdown();
		this.device_boot();
	}

}
