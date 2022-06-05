// [Inventory](https://fabricmc.net/wiki/tutorial:inventory)
package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.minecraft.nbt.CompoundTag;

// implements ImplementedInventory
public class ItemGeneratorEntity extends DeviceEntity {
	// private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

	public ItemGeneratorEntity() {
		super(InfageBlockEntities.ITEM_GENERATOR);
		this.storageSize = 2048;
	}



	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return tag;
	}

	// @Override
	// public DefaultedList<ItemStack> getItems() {
	// 	return this.items;
	// }
}
