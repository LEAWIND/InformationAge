package net.leawind.infage.script.obj;

import net.leawind.infage.block.Switch;
import net.leawind.infage.blockentity.DeviceEntity;

public class SwitchObj extends DeviceObj {
	public static final String type = Switch.BLOCK_ID;

	public SwitchObj(DeviceEntity blockEntity) {
		super(blockEntity);
	}

}
