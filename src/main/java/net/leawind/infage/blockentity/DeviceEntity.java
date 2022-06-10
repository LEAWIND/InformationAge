package net.leawind.infage.blockentity;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.script.CompiledScript;
import net.leawind.infage.Infage;
import net.leawind.infage.exception.InfageDevicePortsNotMatchException;
import net.leawind.infage.script.CompileStatus;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.infage.script.mtt.CompileTask;
import net.leawind.infage.script.mtt.ExecuteTask;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.util.DataEncoding;
import net.leawind.infage.util.Others;
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

	public String receiveCaches[]; // 各接口的接收缓存 //TODO toByte
	public String sendCaches[]; // 各接口的发送缓存 //TODO toByte
	public String script_tick = ";"; // tick 脚本
	public CompiledScript compiledScript_tick = null; // 编译后的 tick 脚本
	public CompileStatus compileStatus = CompileStatus.UNKNOWN; // 编译状态

	// 初始化
	public void init() {
		this.storage = new byte[this.storageSize];
		this.portsStatus = new byte[this.portsCount];

		this.portsX = new long[this.portsCount];
		this.portsY = new long[this.portsCount];
		this.portsZ = new long[this.portsCount];

		this.sendCaches = new String[this.portsCount];
		this.receiveCaches = new String[this.portsCount];
		for (int i = 0; i < this.portsCount; i++) {
			this.receiveCaches[i] = "";
			this.sendCaches[i] = "";
		}
	}

	public DeviceEntity(BlockEntityType<?> type) {
		super(type);
	}

	/*
	 * 要实现在方块实体中储存数据，就要能够加载和保存数据。 在 1.16.5 中是 toTag() 和 fromTag() 1.16.5 参考
	 * [https://fabricmc.net/wiki/tutorial:blockentity?rev=1563817083]
	 * 
	 * 在 1.18 中标识符变成了 writeNbt() 和 readNbt() 1.18 参考 [https://fabricmc.net/wiki/tutorial:blockentity]
	 */
	// 生成实体方块标签用于保存
	@Override
	public synchronized CompoundTag toTag(CompoundTag tag) {
		tag = super.toTag(tag);
		this.init();
		tag.putInt("tickCounter", this.tickCounter);
		tag.putBoolean("isRunning", this.isRunning);
		tag.putInt("storageSize", this.storageSize);
		tag.putInt("portsCount", this.portsCount);

		tag.putString("consoleOutputs", this.consoleOutputs == null ? "" : this.consoleOutputs);
		tag.putString("script_tick", this.script_tick == null ? "" : this.script_tick);

		tag.putString("sendCaches", DataEncoding.encodeStringArray(this.sendCaches));
		tag.putString("receiveCaches", DataEncoding.encodeStringArray(this.receiveCaches));

		tag.putByteArray("storage", this.storage);
		tag.putByteArray("portsStatus", this.portsStatus);

		tag.putLongArray("portsX", this.portsX);
		tag.putLongArray("portsY", this.portsY);
		tag.putLongArray("portsZ", this.portsZ);

		return tag;
	}

	// 加载实体方块标签
	@Override
	public synchronized void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.tickCounter = tag.getInt("tickCounter");
		this.isRunning = tag.getBoolean("isRunning");
		this.storageSize = tag.getInt("storageSize");
		this.portsCount = tag.getInt("portsCount");

		this.setOutputs(tag.getString("consoleOutputs"));
		this.setScirpt_tick(tag.getString("script_tick"));

		this.setStorage(tag.getByteArray("storage"));
		this.setPortsStatus(tag.getByteArray("portsStatus"));

		this.portsX = Others.arrayFrom(tag.getLongArray("portsX"), this.portsCount);
		this.portsY = Others.arrayFrom(tag.getLongArray("portsY"), this.portsCount);
		this.portsZ = Others.arrayFrom(tag.getLongArray("portsZ"), this.portsCount);

		this.sendCaches = Others.arrayFrom(DataEncoding.decodeStringArray(tag.getString("sendCaches")), this.portsCount);
		this.receiveCaches = Others.arrayFrom(DataEncoding.decodeStringArray(tag.getString("receiveCaches")), this.portsCount);
	}

	// 方块实体刻
	@Override
	public void tick() {
		this.tickCounter++;
		if (this.isRunning) // 如果设备在运行
			this.deviceTick(); // 执行设备刻
	}

	// 获取指定接口所连接的设备方块实体
	public DeviceEntity getConnectedDevice(int portId) {
		if (this.portsStatus == null || this.portsStatus[portId] < 0)
			return null;
		if (portId >= this.portsCount || this.portsX == null || this.portsY == null || this.portsZ == null)
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
		this.compileStatus = CompileStatus.UNKNOWN;
		this.script_tick = str == null ? "" : str;
	}

	// 设置端口状态
	public synchronized void setPortsStatus(byte[] arr) {
		if (this.portsStatus == null || this.portsStatus.length != this.portsCount)
			this.portsStatus = new byte[this.portsCount];
		if (arr == null || arr.length == 0) {
			Arrays.fill(this.portsStatus, (byte) 0);
		} else {
			if (arr.length < this.portsCount)
				Arrays.fill(this.portsStatus, arr.length, this.portsCount, (byte) 0);
			System.arraycopy(arr, 0, this.portsStatus, 0, arr.length);
		}
	}

	// 设置本地存储
	public synchronized void setStorage(byte[] arr) {
		if (this.storage == null || this.storage.length != this.storageSize)
			this.storage = new byte[this.storageSize];
		if (arr == null || arr.length == 0) {
			Arrays.fill(this.storage, (byte) 0);
		} else {
			if (arr.length < this.storageSize)
				Arrays.fill(this.storage, arr.length, this.storageSize, (byte) 0);
			System.arraycopy(arr, 0, this.storage, 0, this.storageSize);
		}
	}

	// 设置本地存储
	public synchronized void setStorage(String str) throws UnsupportedEncodingException {
		if (str == null)
			str = "";
		this.setStorage(str.getBytes("UTF-8"));
	}

	// 设置输出信息
	public synchronized void setOutputs(String str) {
		if (str == null) {
			this.consoleOutputs = "";
		} else {
			int leng = str.length() - Infage.OUTPUTS_SIZE;
			this.consoleOutputs = leng > 0 ? str.substring(leng, Infage.OUTPUTS_SIZE) : str; // 如果过长，则保留后面部分
		}
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
	public synchronized DeviceObj getDeviceObj() {
		return new DeviceObj(this);
	}

	// 在执行完脚本之后应用 脚本对 obj 所做的修改
	public synchronized void applyObj(DeviceObj obj) {
		// this.db_checkPortsCount();
		this.setStorage(obj.storage);
		this.setSendCaches(obj.dataToSend);
		if (obj.outputs != null)
			this.writeOutputs(obj.outputs);
	}

	// 设备刻
	public void deviceTick() {
		switch (this.compileStatus) {
			case UNKNOWN: // 还没编译
				this.compileStatus = CompileStatus.DISTRIBUTED;
				// 布置任务:编译
				CompileTask task = new CompileTask(this);
				ScriptHelper.MTM_COMPILE.addTask(task);
				break;
			case DISTRIBUTED: // 已经布置任务,暂未完成
				break;
			case SUCCESS: // 编译已完成
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

	// 将所有要发送的数据写到目标方块实体的接收缓存中
	public void sendAllData() {
		// this.db_checkPortsCount();
		for (int i = 0; i < this.portsCount; i++) {
			if (this.portsStatus[i] >= 0 && this.sendCaches[i] != null && this.sendCaches[i] != "") {
				BlockEntity targetEntity = this.getConnectedDevice(i);
				if (targetEntity != null && targetEntity instanceof DeviceEntity) {
					((DeviceEntity) targetEntity).receiveCaches[this.portsStatus[i]] = this.sendCaches[i]; // 写入
				} else {
					this.portsStatus[i] = -1;
				}
			}
		}
	}

	// 开机
	public synchronized void device_boot() {
		this.isRunning = true;
		this.tickCounter = 0;
	}

	// 关机
	public synchronized void device_shutdown() {
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

	public void db_checkPortsCount() {
		try {
			if (this.portsStatus == null//
					|| this.sendCaches == null//
					|| this.receiveCaches == null//
					|| this.portsX == null//
					|| this.portsY == null//
					|| this.portsZ == null//
			)
				throw new NullPointerException();
			if (this.portsCount != this.portsStatus.length //
					|| this.portsCount != this.sendCaches.length //
					|| this.portsCount != this.receiveCaches.length//
					|| this.portsCount != this.portsX.length//
					|| this.portsCount != this.portsY.length//
					|| this.portsCount != this.portsZ.length//
			)
				throw new InfageDevicePortsNotMatchException(this.portsCount);
		} catch (NullPointerException | InfageDevicePortsNotMatchException e) {
			e.printStackTrace();
		}
	}
}

