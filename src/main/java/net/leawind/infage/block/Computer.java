package net.leawind.infage.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.ComputerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class Computer extends DeviceBlock {
	public static final String BLOCK_ID = "computer";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.DEFAULT_BLOCK_SETTINGS;
	public static final FabricItemSettings BLOCKITEM_SETTINGS = DeviceBlock.DEFAULT_BLOCKITEM_SETTINGS;

	public Computer() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {
				Block.createCuboidShape(1F, 0.4F, 2.18F, 15F, 9F, 15F), // North
				Block.createCuboidShape(1F, 0.4F, 1F, 15F, 9F, 13.82F), // South
				Block.createCuboidShape(1F, 0.4F, 1F, 13.82F, 9F, 15F), // East
				Block.createCuboidShape(2.18F, 0.4F, 1F, 15F, 9F, 15F) // West
		};
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ComputerEntity();
	}
}