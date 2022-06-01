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
	public static final FabricBlockSettings BLOCK_SETTINGS = DeviceBlock.getDefaultBlockSettings();
	public static final FabricItemSettings BLOCKITEM_SETTINGS = DeviceBlock.getDefaultBlockItemSettings();

	public Computer() {
		super(BLOCK_SETTINGS
				.luminance(6) // 亮度
		);
		this.shapes = new VoxelShape[] {
				// 这里的形状现在是一个简单的立方体，但实际上这样并不真实。
				// 其实可以实现把它设置为像楼梯 stair 那样，由多个立方体组合起来的形状。
				// 但是我还没有找到方法 :(
				Block.createCuboidShape(1, 0.4, 2.18, 15, 9, 15), // 北
				Block.createCuboidShape(1, 0.4, 1, 15, 9, 13.82), // 南
				Block.createCuboidShape(1, 0.4, 1, 13.82, 9, 15), // 东
				Block.createCuboidShape(2.18, 0.4, 1, 15, 9, 15), // 西
		};
	}

	// 要想 "在方块中存储数据", 就要能够通过方块获取方块对应的方块实体(根据方块坐标来获取)
	// 当没有对应的方块实体时要能够创建一个方块实体
	// 所以要覆写这个创建对应方块实体的方法
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		this.deviceEntity = new ComputerEntity();
		return this.deviceEntity;
	}
}