package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.shape.VoxelShape;

public class Switch extends DeviceBlock {
	public static final String BLOCK_ID = "switch";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.DEFAULT_BLOCK_SETTINGS;
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.DEFAULT_BLOCKITEM_SETTINGS;

	public Switch() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {
				Block.createCuboidShape(0, 0, 3, 16, 8, 13), // 北
				Block.createCuboidShape(0, 0, 3, 16, 8, 13), // 南
				Block.createCuboidShape(3, 0, 0, 13, 8, 16), // 东
				Block.createCuboidShape(3, 0, 0, 13, 8, 16), // 西
		};
	}
}
