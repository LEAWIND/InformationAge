package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class PowerSensorEntity extends DeviceEntity {

	public PowerSensorEntity() {
		super(InfageBlockEntities.POWER_SENSOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", "power_sensor");
		CompoundTag scriptsTag = tag.getCompound("scripts");
		scriptsTag.putString("powerChange", ""); // 充能状态发生变化
		return tag;
	}
}
