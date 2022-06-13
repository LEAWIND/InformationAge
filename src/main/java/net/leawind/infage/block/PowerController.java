package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.PowerControllerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PowerController extends DeviceBlock {
	public static final String BLOCK_ID = "power_controller";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.getDefaultBlockSettings();
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.getDefaultBlockItemSettings();

	public PowerController() {
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
		return new PowerControllerEntity();
	}

	// 获取比较器输出
	// 用比较器的输入端对准这个方块，比较器输出的信号强度就是这个函数的返回值
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos); // 根据坐标获取对应的方块实体
		return (blockEntity instanceof PowerControllerEntity) && ((DeviceEntity) blockEntity).isRunning ? //
				((PowerControllerEntity) blockEntity).powerLevel : //
				0;
	}

	// 是否有比较器输出
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	// 当方块在服务端上被加入时
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PowerControllerEntity)
			((PowerControllerEntity) blockEntity).updateComparators();
	}
}
