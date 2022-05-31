package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.shape.VoxelShape;

public class Disk extends DeviceBlock {
	public static final String BLOCK_ID = "disk";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.DEFAULT_BLOCK_SETTINGS;
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.DEFAULT_BLOCKITEM_SETTINGS;

	public Disk() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {
				Block.createCuboidShape(0, 0, 2, 16, 14, 14), // 北
				Block.createCuboidShape(0, 0, 2, 16, 14, 14), // 南
				Block.createCuboidShape(2, 0, 0, 14, 14, 16), // 东
				Block.createCuboidShape(2, 0, 0, 14, 14, 16), // 西
		};
	}
}
