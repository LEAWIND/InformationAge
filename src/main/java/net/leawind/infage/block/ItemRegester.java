package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.ItemRegesterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ItemRegester extends DeviceBlock {
	public static final String BLOCK_ID = "item_regester";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.getDefaultBlockSettings();
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.getDefaultBlockItemSettings();

	public ItemRegester() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {//
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 北
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 南
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 东
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 西
		};
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ItemRegesterEntity();
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRegesterEntity) {
				ItemScatterer.spawn(world, pos, (ItemRegesterEntity) blockEntity);
				world.updateComparators(pos, this); // 更新比较器
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
}
