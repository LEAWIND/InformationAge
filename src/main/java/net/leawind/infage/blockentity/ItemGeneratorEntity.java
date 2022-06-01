package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class ItemGeneratorEntity extends DeviceEntity {
	public int storageSize = 2048;

	public ItemGeneratorEntity() {
		super(InfageBlockEntities.ITEM_GENERATOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("storageSize", this.storageSize);
		return tag;
	}
}
