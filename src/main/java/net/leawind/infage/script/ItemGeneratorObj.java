package net.leawind.infage.script;

import net.leawind.infage.Infage;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.util.math.BlockPos;

public class ItemGeneratorObj extends DeviceObj {
	public static final String type = "item_generator";
	public int itemSlotCount = Infage.DEVICE_INVENTORY_SIZE;
	private BlockPos pos;

	public ItemGeneratorObj(DeviceEntity that) {
		super(that);
		this.pos = that.getPos();
	}

	public boolean token2item(byte token) {
		return true;
	}
}
