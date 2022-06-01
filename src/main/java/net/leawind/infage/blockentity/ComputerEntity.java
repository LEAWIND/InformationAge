package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class ComputerEntity extends DeviceEntity {
	public int portsCount = 2;
	public byte portsStatus[] = new byte[] { 0, 0 };
	public int cycleLength = 4;
	public int storageSize = 2048;

	public ComputerEntity() {
		super(InfageBlockEntities.COMPUTER);
	}
}
