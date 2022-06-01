package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerSensorEntity extends DeviceEntity {
	public String script_powerChange = ""; // 充能状态变化

	public PowerSensorEntity() {
		super(InfageBlockEntities.POWER_SENSOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		CompoundTag scriptsTag = tag.getCompound("scripts");
		scriptsTag.putString("powerChange", this.script_powerChange);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		CompoundTag scriptsTag = tag.getCompound("scripts");
		this.script_powerChange = scriptsTag.getString("powerChange");
	}
}
