package net.leawind.infage.blockentity;

import net.leawind.infage.block.DeviceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

// 这是一个方块实体，要继承于 BlockEntity
public class DeviceEntity extends BlockEntity {
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
		// 在这定义了设备的一些共同属性. 可以在子类的这个方法中更详细地定义
		tag.putString("deviceType", DeviceBlock.BLOCK_ID); // 设备类型
		tag.putBoolean("isRunning", false); // 是否正在运行
		tag.putInt("portsCount", 1); // 接口数量 (最多同时有多少个设备和它相连接)
		tag.putInt("cycleLength", -1); // 周期长度 (每隔一个周期触发一次 clock 事件), 小于 0 代表永远不触发
		tag.putInt("storageSize", 64); // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )
		tag.putString("storage", ""); // 磁盘内容
		tag.putByteArray("portsStatus", new byte[] { 0 }); // 各接口状态，0表示未连接, 1 表示已连接
		// 接口发送缓存
		// 事件脚本
		CompoundTag scriptsTag = new CompoundTag();
		scriptsTag.putString("boot", ""); // 启动
		scriptsTag.putString("clock", ""); // 周期性执行
		scriptsTag.putString("portConnected", ""); // 端口连接
		scriptsTag.putString("portDisonnected", ""); // 端口断开
		scriptsTag.putString("receiveData", ""); // 端口断开
		tag.put("scripts", scriptsTag); // 各种事件脚本 (Nashorn js)
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}
}
