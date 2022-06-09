package net.leawind.infage.script.obj;

import net.leawind.infage.block.Disk;
import net.leawind.infage.blockentity.DeviceEntity;

public class DiskObj extends DeviceObj {
	public static final String type = Disk.BLOCK_ID;

	public DiskObj(DeviceEntity blockEntity) {
		super(blockEntity);
	}

}
