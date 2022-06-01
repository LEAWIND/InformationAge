package net.leawind.infage.blockentity;

import net.leawind.infage.block.PowerControler;
import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class PowerControlerEntity extends DeviceEntity {

	public PowerControlerEntity() {
		super(InfageBlockEntities.POWER_CONTROLER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", PowerControler.BLOCK_ID);
		tag.putBoolean("isPowerd", false); // 是否处于充能状态。如果是，则可以用比较器输出信号
		return tag;
	}
}
