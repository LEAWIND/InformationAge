package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.PowerSensorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PowerSensor extends DeviceBlock {
	public static final String BLOCK_ID = "power_sensor";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.getDefaultBlockSettings();
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.getDefaultBlockItemSettings();

	public PowerSensor() {
		super(BLOCK_SETTINGS);
		this.shapes = new VoxelShape[] {//
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 北
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 南
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 东
				Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 西
		};
	}

	// 邻居更新时
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PowerSensorEntity)
			((PowerSensorEntity) blockEntity).isPowered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new PowerSensorEntity();
	}
}
