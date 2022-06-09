package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.script.obj.PowerSensorObj;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerSensorEntity extends DeviceEntity {
	public boolean isPowered = false;
	public PowerSensorEntity() {
		super(InfageBlockEntities.POWER_SENSOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag = super.toTag(tag);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new PowerSensorObj(this);
	}
}
