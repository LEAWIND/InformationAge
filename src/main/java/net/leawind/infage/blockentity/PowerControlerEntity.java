package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class PowerControlerEntity extends DeviceEntity {
	public boolean isPowered = false; // 是否被充能

	public PowerControlerEntity() {
		super(InfageBlockEntities.POWER_CONTROLER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putBoolean("isPowered", this.isPowered);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.isPowered = tag.getBoolean("isPowered");
	}

}
