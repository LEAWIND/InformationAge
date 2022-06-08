package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.PowerControlerObj;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerControlerEntity extends DeviceEntity {
	public int powerLevel = 1; //

	public PowerControlerEntity() {
		super(InfageBlockEntities.POWER_CONTROLER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("powerLevel", this.powerLevel);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.powerLevel = tag.getInt("powerLevel");
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new PowerControlerObj(this);
	}

}
