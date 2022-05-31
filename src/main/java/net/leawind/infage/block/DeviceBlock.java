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

// 设备方块:全都是有水平方向的
public abstract class DeviceBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	// 用于命名空间的 方块ID (infage:defaultid)
	public static final String BLOCK_ID = "i_forgot_to_set_this_id";
	// 默认值: 不同方向下的 shape
	public static final VoxelShape[] DEFAULT_SHAPES = {
			Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F), // North
			Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F), // South
			Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F), // East
			Block.createCuboidShape(0F, 0F, 0F, 16F, 16F, 16F), // West
	};
	// 默认值: 方块属性设置
	// 参考
	// [https://maven.fabricmc.net/docs/fabric-api-0.32.5+1.16/net/fabricmc/fabric/api/object/builder/v1/block/FabricBlockSettings.html]
	public static final FabricBlockSettings DEFAULT_BLOCK_SETTINGS = FabricBlockSettings.of(Material.METAL)
			.sounds(BlockSoundGroup.METAL) // 金属声音
			.hardness(0.5F) // 硬度
			.resistance(8.0F) // 抗性
			.luminance(4) // 亮度
			.breakByHand(true) // 可以用手撸掉
			.collidable(true)// 与实体碰撞
			.nonOpaque() // 透明
	;

	// 默认值: 方块物品设置
	public static final FabricItemSettings DEFAULT_BLOCKITEM_SETTINGS = new FabricItemSettings()
			.maxCount(1) // 最大堆叠数量
			.fireproof(); // 是否防火

	// 形状
	public VoxelShape[] shapes;

	public DeviceBlock(FabricBlockSettings settings) {
		super(settings);
		this.shapes = DEFAULT_SHAPES;
	}

	// 带水平方向
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

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return null;
	}

	// 使用事件
	// 放置事件
	// 破坏事件

}
