// 方块：计算机
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
				Block.createCuboidShape(1, 0.4, 2.18, 15, 9, 15), // 北
				Block.createCuboidShape(1, 0.4, 1, 15, 9, 13.82), // 南
				Block.createCuboidShape(1, 0.4, 1, 13.82, 9, 15), // 东
				Block.createCuboidShape(2.18, 0.4, 1, 15, 9, 15) // 西
		};
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ComputerEntity();
	}
}