// https://www.bilibili.com/read/cv11964630/?from=readlist

package net.leawind.infage.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.gui.InfageDeviceScreen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// 设备方块:全都是有水平方向的 (东西南北), 所以继承 HorizontalFacingBlock
// 因为设备方块一定有对应的 方块实体, 所以要实现 BlockEntityProvider 接口
public class DeviceBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final String BLOCK_ID = "i_forgot_to_set_this_id"; // 用于命名的方块ID (infage:block_id)
	// 实例属性
	public DeviceEntity deviceEntity = null;

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
			.luminance(2) // 亮度
			.breakByHand(true) // 可用手撸掉
			.collidable(true)// 可与实体碰撞
			.nonOpaque() // 透明
	;

	// 默认值: 方块对应物品 的属性设置
	public static final FabricItemSettings DEFAULT_BLOCKITEM_SETTINGS = new FabricItemSettings()
			.maxCount(1) // 最大堆叠数量
			.fireproof(); // 防火

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

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos blockPos,
			PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		try {
			// 如果任意参数为 null 或玩家啊在旁观者模式，则不处理
			if (player == null || player.isSpectator() || world == null || hitResult == null)
				return ActionResult.PASS;
			BlockState blockstate = world.getBlockState(blockPos); // 根据坐标获取 blockState
			Block block = blockstate.getBlock(); // 由 blockState 获取方块对象
			if (block instanceof DeviceBlock) { // 检查这个方块是不是这个模组里定义的 设备
				// 根据坐标获取这个方块的方块实体
				DeviceEntity deviceEntity = (DeviceEntity) world.getBlockEntity(blockPos);
				System.out.printf("\nEvent UseBlockCallback:\n\tPlayer:\n");
				System.out.printf("\t\t%s\n", player);
				System.out.printf("\tused block:\n");
				System.out.printf("\t\t%s\n", block);
				System.out.printf("\tBlockEntity:\n\t\t%s\n", deviceEntity);

				// 判断是不是 客户端
				// 还得看源码
				if (world.isClient) {
					// 客户端
					// player.openCommandBlockScreen(commandBlock);
					System.out.println("================================");
					System.out.println("Open Device Block Screen At Client !!!");
					System.out.println("BlockEntity is");
					System.out.println(deviceEntity);
					// 在客户端显示屏幕
					MinecraftClient.getInstance().openScreen(new InfageDeviceScreen(deviceEntity));

				} else {
					// 服务端
					return ActionResult.SUCCESS;
				}

				return ActionResult.SUCCESS; // 返回了 SUCCESS 就不会处理后续的事件了 (例如放置方块)
			}
		} catch (Exception e) {
		}
		return ActionResult.PASS;
	}

	// 事件：放置
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		// 将状态设置为已关机
		this.device_shutdown();
	}

	// 开机
	public void device_boot() {
		// 检查设备状态
		if (this.deviceEntity.isRunning)
			return;
		// 设置状态为 开机
		this.deviceEntity.isRunning = true;
		// TODO 执行开机脚本
	}

	// 关机
	public void device_shutdown() {
		// 检查设备状态
		if (!this.deviceEntity.isRunning)
			return;
		this.deviceEntity.isRunning = false;
	}

	// 重启
	public void device_reboot() {
		this.device_shutdown();
		this.device_boot();
	}

}
