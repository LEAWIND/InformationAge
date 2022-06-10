package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.script.obj.PowerSensorObj;

public class PowerSensorEntity extends DeviceEntity {
	public boolean isPowered = false;
	public PowerSensorEntity() {
		super(InfageBlockEntities.POWER_SENSOR);
		this.init();
	}


	@Override
	public DeviceObj getDeviceObj() {
		return new PowerSensorObj(this);
	}
}
