package net.leawind.infage.blockentity;

import java.util.Arrays;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.ScriptHandler;
import net.leawind.infage.util.DataEncoding;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

// 这是一个方块实体，要继承于 BlockEntity
public class DeviceEntity extends BlockEntity implements Tickable {
	// 实例属性

	public int tickCounter = 0;
	public boolean isRunning = false; // 是否正在运行
	public int portsCount = 1; // 接口数量 (最多同时有多少个设备和它相连接)
	public int tickInterval = -1; // 周期长度 (每隔一个周期触发一次 clock 事件), 小于 0 代表永远不触发
	public int storageSize = 64; // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )
	public String storage = ""; // 磁盘内容
	public byte portsStatus[]; // 各接口的 目标接口的 id, 若未连接则为 -1
	// 各接口所连接方块的坐标(不一定正确，因为对方可能被破坏)
	public long portsX[];
	public long portsY[];
	public long portsZ[];

	// 各接口接收缓存 每 tick 只能发送一个传输单元 Infage.MAX_TRANSMISSION_UNIT
	public String sendCaches[];
	public String receiveCaches[];

	public String script_tick = ""; // tick 脚本
	public CompiledScript compiledScript_tick = null; // 编译后的 tick 脚本
	public boolean isCompiling = false;

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
		tag.putInt("portsCount", this.portsCount);
		tag.putInt("tickInterval", this.tickInterval);
		tag.putInt("storageSize", this.storageSize);
		tag.putString("storage", this.storage);

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
		if (this.isRunning) {
			if (this.compiledScript_tick == null) {
				// 如果脚本未编译SGSC
				if (!this.isCompiling) {
					this.isCompiling = false;
					this.compiledScript_tick = net.leawind.infage.script.ScriptHandler.compile(this.script_tick);
				}
			} else if (this.tickInterval > 0 && (this.tickCounter % this.tickInterval == 0)) {
				// 定义一个发送缓存
				String[] sendCaches = new String[this.portsCount];
				for (int i = 0; i < this.portsCount; i++)
					sendCaches[i] = "";
				// 将 对外接收缓存 覆盖到 本机接收缓存。
				String[] tp = this.sendCaches;
				this.sendCaches = this.receiveCaches;
				this.receiveCaches = tp;
				// *检查充能状态
				// *检查库存
				// if(this instanceof In)

				// 执行 tick 脚本
				DeviceObj deviceObj = new DeviceObj(this); // 获取当前设备实体的`DeviceObj`实例
				ScriptHandler.BINDINGS.put("Device", deviceObj); // 设置变量绑定

				if (true)
					try {
						this.compiledScript_tick.eval(ScriptHandler.BINDINGS);

						// 如果过长 storage, 则将 js 中的 storage 截取 StorageSize 长度，并覆盖到本方块实体的 storage 属性。
						if (deviceObj.storage.length() > this.storageSize)
							this.storage = deviceObj.storage.substring(0, this.storageSize);

						// 将 deviceObj 的发送缓存 覆盖到 this 的发送缓存
						sendCaches = deviceObj.dataToSend;

						// 将 发送缓存 中的内容覆盖到目标的 对外接收缓存中。
						for (int i = 0; i < this.portsCount; i++) {
							if (this.portsStatus[i] >= 0 && sendCaches[i] != "") {
								// 如果接口已连接，而且发送缓存中有数据
								// 目标位置
								BlockPos targetPos = new BlockPos(this.portsX[i], this.portsY[i], this.portsZ[i]);
								// 获取 目标方块实体
								BlockEntity targetBlockEntity = world.getBlockEntity(targetPos);
								if (targetBlockEntity != null && targetBlockEntity instanceof DeviceEntity) {
									// 如果获取到了方块实体
									// 将要发送的数据填到目标的接收缓存里
									((DeviceEntity) targetBlockEntity).receiveCaches[i] = sendCaches[i];
								}
							}
						}

					} catch (ScriptException e) {
					}
				// 清空对外接收缓存
				Arrays.fill(this.sendCaches, "");
			}
		}
		this.tickCounter++;
	}

	// 开机
	public void device_boot() {
		this.isRunning = true;
		this.tickCounter = 0;
	}

	// 关机
	public void device_shutdown() {
		this.isRunning = false;
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
