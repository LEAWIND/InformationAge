package net.leawind.infage.script.obj;

import net.leawind.infage.block.PowerController;
import net.leawind.infage.blockentity.DeviceEntity;

public class PowerControllerObj extends DeviceObj {
	public static final String type = PowerController.BLOCK_ID;
	public int powerLevel;

	// TODO
	public PowerControllerObj(DeviceEntity blockEntity) {
		super(blockEntity);
		this.powerLevel = 0;
	}


}
