package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;

// 直接从 BlockEntity 继承得了
public class ComputerEntity extends DeviceEntity {
	public ComputerEntity() {
		super(InfageBlockEntities.COMPUTER);
	}

}
