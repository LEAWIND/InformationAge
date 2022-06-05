package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class ComputerEntity extends DeviceEntity {
	public ComputerEntity() {
		super(InfageBlockEntities.COMPUTER);
		this.portsCount = 2;
		this.tickInterval = 4;
		this.storageSize = 2048;
		this.initPorts();
	}
}
