package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.PowerControllerObj;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerControllerEntity extends DeviceEntity {
	public int powerLevel = 1; //

	public PowerControllerEntity() {
		super(InfageBlockEntities.POWER_CONTROLLER);
	}

	@Override
	public void applyObj(DeviceObj obj) {
		super.applyObj(obj);
		// 更新
		boolean doUpdateComparators = this.powerLevel != ((PowerControllerObj) obj).powerLevel;
		this.powerLevel = ((PowerControllerObj) obj).powerLevel;
		if (doUpdateComparators) {
			this.getWorld().updateComparators(this.getPos(), InfageBlocks.POWER_CONTROLLER);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		// tag.putInt("powerLevel", this.powerLevel);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		// this.powerLevel = tag.getInt("powerLevel");
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new PowerControllerObj(this);
	}

}
