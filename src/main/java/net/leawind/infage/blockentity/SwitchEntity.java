package net.leawind.infage.blockentity;

import net.leawind.infage.block.Switch;
import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class SwitchEntity extends DeviceEntity {

	public SwitchEntity() {
		super(InfageBlockEntities.SWITCH);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", Switch.BLOCK_ID);

		tag.putInt("portsCount", 16);
		tag.putByteArray("portsStatus", new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });

		tag.putInt("cycleLength", 8);
		tag.putInt("storageSize", 1024); // 可以在游戏中用脚本存储的数据量最大值 ( 其实就是 storage 字符串的最大长度 )

		return tag;
	}

}
