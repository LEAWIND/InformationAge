package net.leawind.infage.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

// 直接从 BlockEntity 继承
public class DeviceEntity extends BlockEntity {
	public DeviceEntity(BlockEntityType<?> type) {
		super(type);
	}

}
