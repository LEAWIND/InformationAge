package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class DiskEntity extends DeviceEntity {

	public DiskEntity() {
		super(InfageBlockEntities.DISK);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", "disk");
		tag.putInt("storageSize", 65536);
		tag.putByteArray("portsStatus", new byte[] { 0 });
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}
}
