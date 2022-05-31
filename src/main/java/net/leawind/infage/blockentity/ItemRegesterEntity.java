package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class ItemRegesterEntity extends DeviceEntity {

	public ItemRegesterEntity() {
		super(InfageBlockEntities.ITEM_REGESTER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", "item_regester");
		tag.putInt("storageSize", 256);
		return tag;
	}
}
