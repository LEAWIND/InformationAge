package net.leawind.infage.script;

import net.leawind.infage.blockentity.DeviceEntity;

public class PowerControllerObj extends DeviceObj {
	public static final String type = "power_controler";
	public int powerLevel;

	// TODO
	public PowerControllerObj(DeviceEntity blockEntity) {
		super(blockEntity);
		this.powerLevel = 0;
	}


}
