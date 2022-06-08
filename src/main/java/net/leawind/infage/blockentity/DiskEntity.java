package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.DiskObj;

public class DiskEntity extends DeviceEntity {

	public DiskEntity() {
		super(InfageBlockEntities.DISK);
		this.storageSize = 65536;
	}

	public DeviceObj getDeviceObj() {
		return new DiskObj(this);
	}
}
