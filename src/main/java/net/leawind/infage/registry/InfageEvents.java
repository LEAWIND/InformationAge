package net.leawind.infage.registry;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.leawind.infage.block.DeviceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class InfageEvents {
	public static void register() {
		// 事件：使用方块
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			try {
				// 如果任意参数为 null 或玩家啊在旁观者模式，则不处理
				if (player == null || player.isSpectator() || world == null || hitResult == null)
					return ActionResult.PASS;
				BlockPos blockPos = hitResult.getBlockPos(); // 获取方块坐标
				BlockState blockstate = world.getBlockState(blockPos); // 根据坐标获取 blockState
				Block block = blockstate.getBlock(); // 由 blockState 获取方块对象
				if (block instanceof DeviceBlock) { // 检查这个方块是不是这个模组里定义的 设备
					// 根据坐标获取这个方块的方块实体
					BlockEntity blockEntity = world.getBlockEntity(blockPos);
					System.out.printf("\nEvent UseBlockCallback:\n\tPlayer:\n");
					System.out.printf("\t\t%s\n", player);
					System.out.printf("\tused block:\n");
					System.out.printf("\t\t%s\n", block);
					System.out.printf("\tBlockEntity:\n\t\t%s\n", blockEntity);

					// 判断是不是 客户端
					return ActionResult.SUCCESS; // 返回了 SUCCESS 就不会处理后续的事件了 (例如放置方块)
				}
			} catch (Exception e) {
			}
			return ActionResult.PASS;
		});
	}
}
