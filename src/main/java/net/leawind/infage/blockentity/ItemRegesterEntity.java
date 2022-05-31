package net.leawind.infage.blockentity;

import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

public class ItemRegesterEntity extends DeviceEntity {

	public ItemRegesterEntity() {
		super(InfageBlockEntities.ITEM_REGESTER);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("deviceType", ItemRegester.BLOCK_ID);
		tag.putInt("storageSize", 256);
		return tag;
	}
}
