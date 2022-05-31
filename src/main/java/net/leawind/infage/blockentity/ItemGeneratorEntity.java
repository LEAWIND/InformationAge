package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class ItemGeneratorEntity extends DeviceEntity {

	public ItemGeneratorEntity() {
		super(InfageBlockEntities.ITEM_GENERATOR);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", "item_generator");
		tag.putInt("storageSize", 256);
		return tag;
	}
}
