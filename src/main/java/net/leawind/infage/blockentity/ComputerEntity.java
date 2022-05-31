package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class ComputerEntity extends DeviceEntity {
	public ComputerEntity() {
		super(InfageBlockEntities.COMPUTER);
	}

	// 有关这个方法的详细说明见父类
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", "computer");

		tag.putInt("portsCount", 2);
		tag.putByteArray("portsStatus", new byte[] { 0, 0 });

		tag.putInt("cycleLength", 4);
		tag.putInt("storageSize", 2048);
		tag.putString("storage", "");
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}
}
