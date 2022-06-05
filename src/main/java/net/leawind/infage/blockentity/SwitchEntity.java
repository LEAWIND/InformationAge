package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

public class SwitchEntity extends DeviceEntity {
	public SwitchEntity() {
		super(InfageBlockEntities.SWITCH);
		this.portsCount = 8;
		this.tickInterval = 8;
		this.storageSize = 1024;
		this.initPorts();
	}
}
