package net.leawind.infage.script;

import net.leawind.infage.Infage;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.util.math.BlockPos;

public class ItemRegesterObj extends DeviceObj {
	public static final String type = "item_regester";
	public int itemSlotCount = Infage.DEVICE_INVENTORY_SIZE;
	private BlockPos pos;

	public ItemRegesterObj(DeviceEntity that) {
		super(that);
		this.pos = that.getPos();
	}

	public boolean item2token(int slotId) {
		return false;
	}
}
