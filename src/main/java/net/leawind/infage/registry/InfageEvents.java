package net.leawind.infage.registry;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class InfageEvents {
	public static void register() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			System.out.print("/////////////////////////////////////////////");
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			System.out.println(block);
			System.out.println(block.getClass());
			System.out.println(block.getName());
			return ActionResult.PASS;
		});
	}
}
