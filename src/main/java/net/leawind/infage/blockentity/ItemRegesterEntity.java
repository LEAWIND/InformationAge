package net.leawind.infage.blockentity;

import net.leawind.infage.Infage;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.script.obj.ItemRegesterObj;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class ItemRegesterEntity extends DeviceEntity implements ImplementedInventory, SidedInventory {
	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(Infage.DEVICE_INVENTORY_SIZE, ItemStack.EMPTY);

	public ItemRegesterEntity() {
		super(InfageBlockEntities.ITEM_REGESTER);
		this.storageSize = 256;
		this.init();
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new ItemRegesterObj(this);
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
