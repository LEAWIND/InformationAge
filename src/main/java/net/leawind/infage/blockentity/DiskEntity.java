package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class DiskEntity extends DeviceEntity {
	public int storageSize = 65536;

	public DiskEntity() {
		super(InfageBlockEntities.DISK);
	}
}
