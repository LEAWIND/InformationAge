package net.leawind.infage.script.obj;

import net.leawind.infage.Infage;
import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.util.math.BlockPos;

public class ItemGeneratorObj extends DeviceObj {
	public static final String type = ItemGenerator.BLOCK_ID;
	public int itemSlotCount = Infage.DEVICE_INVENTORY_SIZE;
	private BlockPos pos;

	public ItemGeneratorObj(DeviceEntity that) {
		super(that);
		this.pos = that.getPos();
	}

	// TODO 将令牌兑换为物品
	public boolean token2item(byte token) {
		return true;
	}
}
