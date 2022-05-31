// https://www.bilibili.com/read/cv11964630/?from=readlist

package net.leawind.infage.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

// 设备方块:全都是有水平方向的 (东西南北), 所以继承 HorizontalFacingBlock
// 因为设备方块一定有对应的 方块实体, 所以要实现 BlockEntityProvider 接口
public class DeviceBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final String BLOCK_ID = "i_forgot_to_set_this_id"; // 用于命名的方块ID (infage:block_id)
	// 默认值: 方块不同方向下的 碰撞箱
	// 北南东西 的顺序是我在 getOutlineShape 方法中自定义的
	public static final VoxelShape[] DEFAULT_SHAPES = {
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 北
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 南
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 东
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 西
	};
	// 默认值: 方块 的属性设置
	// 参考
	// [https://maven.fabricmc.net/docs/fabric-api-0.32.5+1.16/net/fabricmc/fabric/api/object/builder/v1/block/FabricBlockSettings.html]
	public static final FabricBlockSettings DEFAULT_BLOCK_SETTINGS = FabricBlockSettings.of(Material.METAL) // 金属材料
			.sounds(BlockSoundGroup.METAL) // 金属声音
			.hardness(0.5F) // 硬度
			.resistance(8.0F) // 抗性
			.luminance(4) // 亮度
			.breakByHand(true) // 可用手撸掉
			.collidable(true)// 可与实体碰撞
			.nonOpaque() // 透明
	;

	// 默认值: 方块对应物品 的属性设置
	public static final FabricItemSettings DEFAULT_BLOCKITEM_SETTINGS = new FabricItemSettings()
			.maxCount(1) // 最大堆叠数量
			.fireproof(); // 是否防火

	// 形状
	public VoxelShape[] shapes;

	public DeviceBlock(FabricBlockSettings settings) {
		super(settings);
		this.shapes = DEFAULT_SHAPES;
	}

	// 加上水平方向
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

	// 碰撞箱 和 边框
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		if (direction.equals(Direction.NORTH)) {
			return this.shapes[0];
		} else if (direction.equals(Direction.SOUTH)) {
			return this.shapes[1];
		} else if (direction.equals(Direction.EAST)) {
			return this.shapes[2];
		} else if (direction.equals(Direction.WEST)) {
			return this.shapes[3];
		}
		return null;
	}

	// 要想 "在方块中存储数据", 就要能够通过方块获取方块对应的方块实体(根据方块坐标来获取)
	// 当没有对应的方块实体时要能够创建一个方块实体
	// 所以要覆写这个创建对应方块实体的方法
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return null;
	}

	// // 开机
	// public void device_boot() {
	// // 检查设备状态
	// // 设置状态为 开机
	// // 执行开机脚本
	// }

	// // 关机
	// public void device_shutdown() {
	// // 检查设备状态
	// // 设置状态为关机
	// }

	// // 重启
	// public void device_reboot() {
	// this.device_shutdown();
	// this.device_boot();
	// }

	// 放置事件
	// 破坏事件

}
