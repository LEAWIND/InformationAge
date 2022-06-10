package net.leawind.infage.blockentity;

import net.leawind.infage.Infage;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.script.obj.ItemGeneratorObj;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

// implements ImplementedInventory
public class ItemGeneratorEntity extends DeviceEntity implements ImplementedInventory {
	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
	public ItemGeneratorEntity() {
		super(InfageBlockEntities.ITEM_GENERATOR);
		this.storageSize = 2048;
		this.init();
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new ItemGeneratorObj(this);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, this.items);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag = super.toTag(tag);
		tag = Inventories.toTag(tag, this.items);
		return tag;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return this.items;
	}

	// 对于 side 侧，哪些物品槽位可以被插入
	public int[] getAvailableSlots(net.minecraft.util.math.Direction side) {
		return Infage.AVAILABLE_SLOTS;
	}

	// 能否在 dir方向 将 物品stack 放入 物品槽slot
	public boolean canInsert(int slot, net.minecraft.item.ItemStack stack, net.minecraft.util.math.Direction dir) {
		return true;
	}

	// 能否在 dir方向 吸出 物品槽slot 的 stack物品
	public boolean canExtract(int slot, net.minecraft.item.ItemStack stack, net.minecraft.util.math.Direction dir) {
		return true;
	}
}
