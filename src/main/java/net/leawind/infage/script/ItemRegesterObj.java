package net.leawind.infage.script;

import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.util.math.BlockPos;

public class ItemRegesterObj extends DeviceObj {
	public static final String type = "item_regester";
	private BlockPos pos;

	public ItemRegesterObj(DeviceEntity that) {
		super(that);
		this.pos = that.getPos();
	}

	public boolean item2token(int slotId) {
		return false;
	}
}
