package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.ComputerObj;
import net.leawind.infage.script.DeviceObj;

public class ComputerEntity extends DeviceEntity {
	public ComputerEntity() {
		super(InfageBlockEntities.COMPUTER);
		this.portsCount = 2;
		this.tickInterval = 4;
		this.storageSize = 2048;
		this.initPorts();
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new ComputerObj(this);
	}
}
