package net.leawind.infage.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.PowerControlerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PowerControler extends DeviceBlock {
	public static final String BLOCK_ID = "power_controler";
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.DEFAULT_BLOCK_SETTINGS;
	public static final Item.Settings BLOCKITEM_SETTINGS = DeviceBlock.DEFAULT_BLOCKITEM_SETTINGS;

	public PowerControler() {
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
		this.deviceEntity = new PowerControlerEntity();
		return this.deviceEntity;
	}

	// 获取比较器输出
	// 应该是用比较器的输入端对准这个方块，比较器输出的信号强度就是这个函数的返回值
	// 但是似乎没有用
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		// return blockEntity instanceof PowerControlerEntity ? 4 : 0;
		if (blockEntity instanceof PowerControlerEntity) {
			return 3;
		} else {
			return 0;
		}
	}
}
