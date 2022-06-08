package net.leawind.infage.blockentity;

import java.util.Arrays;
import javax.script.CompiledScript;
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
	public boolean isRunning = false; // 是否正在运行
	public int portsCount = 1; // 接口数量 (最多同时有多少个设备和它相连接)
	public int tickInterval = -1; // 周期长度 (每隔一个周期触发一次 clock 事件), 小于 0 代表永远不触发
	public int storageSize = 64; // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )
	public String storage = ""; // 磁盘内容
	public String consoleOutputs = ""; // 输出缓冲区
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

	// 初始化 端口相关属性
	public void initPorts() {
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
		initPorts();
	}

	// 获取指定接口所连接的设备方块实体
	public DeviceEntity getConnectedDevice(int portId) {
		if (this.portsStatus[portId] >= 0) {
			BlockPos pos = new BlockPos(this.portsX[portId], this.portsY[portId], this.portsZ[portId]);
			BlockEntity blockEntity = this.getWorld().getBlockEntity(pos);
			if (blockEntity instanceof DeviceEntity)
				return (DeviceEntity) blockEntity;
			else
				return null;
		} else
			return null;
	}

	/*
	 * 要实现在方块实体中储存数据，就要能够加载和保存数据。 在 1.16.5 中是 toTag() 和 fromTag() 1.16.5 参考
	 * [https://fabricmc.net/wiki/tutorial:blockentity?rev=1563817083]
	 * 
	 * 在 1.18 中标识符变成了 writeNbt() 和 readNbt() 1.18 参考 [https://fabricmc.net/wiki/tutorial:blockentity]
	 * 
	 */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		// Infage.LOGGER.info("To Tag ()");
		// 父类的 toTag() 会将方块的 id 和坐标保存到方块实体中。
		// 如果没有这个步骤，以后就无法通过坐标查找到这个方块实体，这些数据就相当于丢失了。
		super.toTag(tag);

		// // 将本实例的属性保存到 tag
		tag.putInt("tickCounter", this.tickCounter);
		tag.putBoolean("isRunning", this.isRunning);
		tag.putInt("tickInterval", this.tickInterval);
		tag.putInt("storageSize", this.storageSize);
		tag.putString("storage", this.storage);
		tag.putString("consoleOutputs", this.consoleOutputs);

		tag.putInt("portsCount", this.portsCount);
		tag.putByteArray("portsStatus", this.portsStatus);
		tag.putLongArray("portsX", this.portsX);
		tag.putLongArray("portsY", this.portsY);
		tag.putLongArray("portsZ", this.portsZ);
		tag.putString("script_tick", this.script_tick);

		// 字符串数组
		tag.putString("sendCaches", DataEncoding.encode(this.sendCaches));
		tag.putString("receiveCaches", DataEncoding.encode(this.receiveCaches));

		return tag;
	}


	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		// Infage.LOGGER.info("From Tag ()");
		// 从tag中读取数据并保存到实例属性中
		super.fromTag(state, tag);
		this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));

		this.tickCounter = tag.getInt("tickCounter");
		this.isRunning = tag.getBoolean("isRunning");
		this.tickInterval = tag.getInt("tickInterval");
		this.storageSize = tag.getInt("storageSize");
		this.storage = tag.getString("storage");
		this.consoleOutputs = tag.getString("consoleOutputs");

		this.portsCount = tag.getInt("portsCount");
		this.portsStatus = tag.getByteArray("portsStatus");
		this.portsX = tag.getLongArray("portsX");
		this.portsY = tag.getLongArray("portsY");
		this.portsZ = tag.getLongArray("portsZ");
		this.script_tick = tag.getString("script_tick"); // tick 脚本

		// 字符串数组
		this.sendCaches = DataEncoding.decode(tag.getString("sendCaches"));
		this.receiveCaches = DataEncoding.decode(tag.getString("receiveCaches"));
	}

	@Override
	public void tick() {
		this.tickCounter++;
		if (this.isRunning) { // 如果设备在运行
			this.deviceTick(); // 执行设备刻
		}
	}

	// 获取当前设备实体的 DeviceObj 实例，用于 定义在脚本中可以使用的 可调用方法 和 可读写属性
	public DeviceObj getDeviceObj() {
		return new DeviceObj(this);
	}

	// 设备刻
	public void deviceTick() {
		// System.out.println("Device block running: " + this.getType());
		if (this.compiledScript_tick == null) { // 如果脚本未编译
			if (!this.isCompiling) {
				this.isCompiling = true;
				// this.compiledScript_tick = net.leawind.infage.script.ScriptHelper.compile(this.script_tick); //
				// 布置任务:编译
				CompileTask compileTask = new CompileTask(this);
				ScriptHelper.MTMANGER.addTask(compileTask);
			}
		} else {
			// 创建新任务
			ExecuteTask executeTask = new ExecuteTask(this);
			// 获取当前设备实体的 DeviceObj 实例，用于在脚本中提供 可调用方法 和 可读写属性
			executeTask.deviceObj = this.getDeviceObj();
			// 创建绑定，并 设置 标识符 与 DeviceObj实例 的对应关系
			executeTask.bindings = ScriptHelper.ENGINE.createBindings();
			executeTask.bindings.put("Device", executeTask.deviceObj); // 在脚本中可以通过 Device 访问这个对象
			// 布置任务:执行
			ScriptHelper.MTMANGER.addTask(executeTask);
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
