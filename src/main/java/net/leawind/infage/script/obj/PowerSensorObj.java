package net.leawind.infage.script.obj;

import net.leawind.infage.block.PowerSensor;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.PowerSensorEntity;

public class PowerSensorObj extends DeviceObj {
	public static final String type = PowerSensor.BLOCK_ID;
	public boolean isPowered;

	public PowerSensorObj(DeviceEntity blockEntity) {
		super(blockEntity);
		this.isPowered = ((PowerSensorEntity) blockEntity).isPowered;
	}

}
