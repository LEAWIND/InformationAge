package net.leawind.infage.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

// 这是一个方块实体，要继承于 BlockEntity
public class DeviceEntity extends BlockEntity implements Tickable {
	// 实例属性
	public boolean isRunning = false; // 是否正在运行
	public int portsCount = 1; // 接口数量 (最多同时有多少个设备和它相连接)
	public int cycleLength = -1; // 周期长度 (每隔一个周期触发一次 clock 事件), 小于 0 代表永远不触发
	public int storageSize = 64; // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )
	public String storage = ""; // 磁盘内容
	public byte portsStatus[] = new byte[] { 0 }; // 各接口状态，0表示未连接, 1 表示已连接
	public String script_boot = ""; // 启动
	public String script_clock = ""; // 周期性执行
	public String script_portConnect = ""; // 端口连接
	public String script_portDisconnect = ""; // 端口断开
	public String script_receiveData = ""; // 端口收到数据

	public DeviceEntity(BlockEntityType<?> type) {
		super(type);
	}

	/*
	 * 要实现在方块实体中储存数据，就要能够加载和保存数据。
	 * 在 1.18 中只需要覆写 writeNbt() 和 readNbt() 即可
	 * 1.18 参考 [https://fabricmc.net/wiki/tutorial:blockentity]
	 * 
	 * 但是在 1.16.5 中不一样，是 totag 和 fromTag
	 * 1.16.5 参考 [https://fabricmc.net/wiki/tutorial:blockentity?rev=1563817083]
	 */

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		// 父类的 toTag() 会将方块的 id 和坐标保存到方块实体中。
		// 如果没有 id 和坐标信息, 这个方块实体的数据就会丢失
		// 所以必须要先调用一下父类的这个方法
		super.toTag(tag);
		// 将本实例的属性保存到 tag

		tag.putBoolean("isRunning", this.isRunning);
		tag.putInt("portsCount", this.portsCount);
		tag.putInt("cycleLength", this.cycleLength);
		tag.putInt("storageSize", this.storageSize);
		tag.putString("storage", this.storage);
		tag.putByteArray("portsStatus", this.portsStatus);
		CompoundTag scriptsTag = new CompoundTag();
		scriptsTag.putString("boot", this.script_boot);
		scriptsTag.putString("clock", this.script_clock);
		scriptsTag.putString("portConnected", this.script_portConnect);
		scriptsTag.putString("portDisconnected", this.script_portDisconnect);
		scriptsTag.putString("receiveData", this.script_receiveData);
		tag.put("scripts", scriptsTag); // 各种事件脚本 (Nashorn js)
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		// 从tag中读取数据并保存到实例属性中
		super.fromTag(state, tag);
		this.isRunning = tag.getBoolean("isRunning");
		this.portsCount = tag.getInt("portsCount");
		this.cycleLength = tag.getInt("cycleLength");
		this.storageSize = tag.getInt("storageSize");
		this.storage = tag.getString("storage");
		this.portsStatus = tag.getByteArray("portsStatus");
		CompoundTag scriptsTag = tag.getCompound("scripts");
		this.script_boot = scriptsTag.getString("boot");
		this.script_clock = scriptsTag.getString("clock");
		this.script_portConnect = scriptsTag.getString("portConnected");
		this.script_portDisconnect = scriptsTag.getString("portDisconnected");
		this.script_receiveData = scriptsTag.getString("receiveData");
	}

	// 这个在加载了方块实体之后才会有用
	// 刚进入世界时，并不是所有的方块实体都已加载，大部分方块实体都还没有加载。
	// public int tickCounter = 0;
	@Override
	public void tick() {
		// TODO Device on Tick
		if (!this.isRunning)
			return;
		// this.tickCounter++;
		// if (this.tickCounter % 4 == 0)
		// System.out.printf("Tick count is [%d]\n", this.tickCounter);
	}
}
