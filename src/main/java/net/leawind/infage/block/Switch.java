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
				Block.createCuboidShape(0F, 0F, 3F, 16F, 8F, 13F), // North
				Block.createCuboidShape(0F, 0F, 3F, 16F, 8F, 13F), // South
				Block.createCuboidShape(3F, 0F, 0F, 13F, 8F, 16F), // East
				Block.createCuboidShape(3F, 0F, 0F, 13F, 8F, 16F), // West
		};
	}
}
