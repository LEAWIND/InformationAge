package net.leawind.infage.script;

import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.PowerSensorEntity;

public class PowerSensorObj extends DeviceObj {
	public static final String type = "power_sensor";
	public boolean isPowered;

	public PowerSensorObj(DeviceEntity blockEntity) {
		super(blockEntity);
		this.isPowered = ((PowerSensorEntity) blockEntity).isPowered;
	}

}
