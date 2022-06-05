package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class DiskEntity extends DeviceEntity {

	public DiskEntity() {
		super(InfageBlockEntities.DISK);
		this.storageSize = 65536;
	}
}
