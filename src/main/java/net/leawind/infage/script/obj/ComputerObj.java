package net.leawind.infage.script.obj;

import net.leawind.infage.block.Computer;
import net.leawind.infage.blockentity.DeviceEntity;

public class ComputerObj extends DeviceObj {
	public static final String type = Computer.BLOCK_ID;

	public ComputerObj(DeviceEntity blockEntity) {
		super(blockEntity);
	}

}
