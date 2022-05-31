package net.leawind.infage.blockentity;

import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class ItemGeneratorEntity extends DeviceEntity {

	public ItemGeneratorEntity() {
		super(InfageBlockEntities.ITEM_GENERATOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", ItemGenerator.BLOCK_ID);
		tag.putInt("storageSize", 256);
		return tag;
	}
}
