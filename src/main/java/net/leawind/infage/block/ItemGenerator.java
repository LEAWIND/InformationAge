package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.ItemGeneratorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ItemGenerator extends DeviceBlock {
	public static final String BLOCK_ID = "item_generator";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.DEFAULT_BLOCK_SETTINGS;
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.DEFAULT_BLOCKITEM_SETTINGS;

	public ItemGenerator() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 北
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 南
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 东
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 西
		};
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		this.deviceEntity = new ItemGeneratorEntity();
		return this.deviceEntity;
	}
}
