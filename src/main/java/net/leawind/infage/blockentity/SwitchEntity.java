package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class SwitchEntity extends DeviceEntity {

	public int portsCount = 16;
	public int cycleLength = 8;
	public int storageSize = 1024;
	public byte portsStatus[] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public SwitchEntity() {
		super(InfageBlockEntities.SWITCH);
	}
}
