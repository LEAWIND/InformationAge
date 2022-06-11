package net.leawind.infage.mixin;

import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Lists;
import net.leawind.infage.Infage;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.ScriptHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {
	@Shadow
	public final List<BlockEntity> tickingBlockEntities = Lists.newArrayList();

	@Inject(at = @At("HEAD"), method = "tickBlockEntities()V")
	private void onTickBlockEntities_head(CallbackInfo info) {

		// 清空所有 exec 任务，并统计数量
		int clearedTasksCount = ScriptHelper.MTM_EXEC.clearTasks();
		if (clearedTasksCount > 0)
			Infage.LOGGER.warn(clearedTasksCount + " exec tasks time out.\n");

		// 在这不应该清空 接收或发送缓存，缓存应该由设备自己 在 tick 中决定何时清空
		Iterator<BlockEntity> blockEntityIterator = tickingBlockEntities.iterator(); // 获取世界中方块实体的迭代器
		while (blockEntityIterator.hasNext()) {
			BlockEntity blockEntity = blockEntityIterator.next(); // 向迭代器获取下一个 方块实体
			if (blockEntity instanceof DeviceEntity && !blockEntity.isRemoved() && blockEntity.hasWorld()) {
				((DeviceEntity) blockEntity).sendAllData();
			}
		}

	}
}

