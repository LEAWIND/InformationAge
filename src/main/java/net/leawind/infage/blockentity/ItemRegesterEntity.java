package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.script.DeviceObj;
import net.leawind.infage.script.ItemRegesterObj;

public class ItemRegesterEntity extends DeviceEntity {

	public ItemRegesterEntity() {
		super(InfageBlockEntities.ITEM_REGESTER);
		this.storageSize = 256;
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new ItemRegesterObj(this);
	}
}
