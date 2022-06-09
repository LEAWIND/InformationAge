package net.leawind.infage.script.obj;

import net.leawind.infage.Infage;
import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.util.math.BlockPos;

public class ItemRegesterObj extends DeviceObj {
	public static final String type = ItemRegester.BLOCK_ID;
	public int itemSlotCount = Infage.DEVICE_INVENTORY_SIZE;
	private BlockPos pos;

	public ItemRegesterObj(DeviceEntity that) {
		super(that);
		this.pos = that.getPos();
	}

	// TODO 将物品兑换为令牌
	public boolean item2token(int slotId) {
		return false;
	}
}
