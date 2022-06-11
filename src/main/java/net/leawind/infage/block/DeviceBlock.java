// https://www.bilibili.com/read/cv11964630/?from=readlist

package net.leawind.infage.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

// 设备方块:全都是有水平方向的 (东西南北), 所以继承 HorizontalFacingBlock
// 因为设备方块一定有对应的 方块实体, 所以要实现 BlockEntityProvider 接口
public abstract class DeviceBlock extends BlockWithEntity {
	public static final String BLOCK_ID = "device_block"; // 用于命名的方块ID (infage:block_id)
	public static final DirectionProperty FACING;

	static {
		FACING = Properties.HORIZONTAL_FACING;
	}
	// 默认值: 方块不同方向下的 碰撞箱
	// 北南东西 的顺序是在 getOutlineShape 方法中自定义的
	public static final VoxelShape[] DEFAULT_SHAPES = {//
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 北
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 南
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 东
			Block.createCuboidShape(0, 0, 0, 16, 16, 16), // 西
	};

	// 默认值: 方块 的属性设置
	// 参考
	// [https://maven.fabricmc.net/docs/fabric-api-0.32.5+1.16/net/fabricmc/fabric/api/object/builder/v1/block/FabricBlockSettings.html]
	public static final FabricBlockSettings getDefaultBlockSettings() {
		return FabricBlockSettings.of(Material.METAL) // 金属材料
				.sounds(BlockSoundGroup.METAL) // 金属声音
				.hardness(0.5F) // 硬度
				.resistance(8.0F) // 抗性
				.luminance(2) // 亮度
				.breakByHand(true) // 可用手撸掉
				.collidable(true)// 可与实体碰撞
				.nonOpaque() // 透明
		;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	// 默认值: 方块对应物品 的属性设置
	public static final FabricItemSettings getDefaultBlockItemSettings() {
		return new FabricItemSettings()//
				.maxCount(1) // 最大堆叠数量
				.fireproof() // 防火
		;
	}

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

	// 如何确定放置方向
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
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return null;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		if (player == null || player.isSpectator() || world == null || hitResult == null) // 如果任意参数为 null 或玩家处于旁观者模式，则不处理
			return ActionResult.PASS;
		// BlockState blockstate = world.getBlockState(pos); // 根据坐标获取 blockState
		// Block block = blockstate.getBlock(); // 由 blockState 获取方块对象
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof DeviceEntity) {
			return ((DeviceEntity) blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
		} else {
			return ActionResult.PASS;
		}
	}

	// 事件：放置
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {}

	// 事件：被破坏
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity instanceof DeviceEntity) {
			// 将状态设置为已关机
			((DeviceEntity) blockEntity).device_shutdown();
		}
	}

	// 当方块在服务端上被加入时
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		// 默认情况下，方块实体不会立即被加载
		world.getBlockEntity(pos); // 这可以确保设备方块实体一开始就被加载
	}
}
