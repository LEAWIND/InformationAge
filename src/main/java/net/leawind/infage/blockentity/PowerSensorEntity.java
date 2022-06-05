package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerSensorEntity extends DeviceEntity {

	public PowerSensorEntity() {
		super(InfageBlockEntities.POWER_SENSOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}
}
