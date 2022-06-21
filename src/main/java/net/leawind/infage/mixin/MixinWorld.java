package net.leawind.infage.mixin;

import java.util.List;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Lists;
import net.leawind.infage.Infage;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.universe.mttv3.MTManager.MTState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {
	@Shadow
	public final List<BlockEntity> tickingBlockEntities = Lists.newArrayList();

	@Inject(at = @At("HEAD"), method = "tickBlockEntities()V")
	private void onTickBlockEntities_head(CallbackInfo info) {
		Infage.increaseBlockEntityTickCounter(); // increment the tick counter every tickingBlockEntities
		// TODO exec timeout
		// exec 任务有可能超时，每个设备实体都有一个脚本超时计数器
		// 每次编译完都将超时计数器置 0
		// 每次成功执行完毕都将计数器 -1
		// 每 tick 中都检查上一 tick 中没有执行完毕的任务，这些任务全都超时了。
		// 对于这些超时的任务，将其对应的实体的超时计数器 +2
		// 并且强制停止这些任务所在线程
		// 如果超时计数器已经超过额定值，则强制停止该线程
		// 清空所有未被领取的 exec 任务，并统计数量
		try {
			if (Infage.isDeviceTickNow()) {

				ScriptHelper.MTM_EXEC.clearTasks();

				int clearedThreadsCount = ScriptHelper.MTM_EXEC.stopThreads(MTState.EXECING);
				if (clearedThreadsCount > 0)
					Infage.LOGGER.warn(clearedThreadsCount + " exec threads time out.\n");

				// 在这不应该清空 接收或发送缓存，缓存应该由设备自己 在 tick 中决定何时清空

				for (BlockEntity blockEntity : tickingBlockEntities) {
					if (blockEntity instanceof DeviceEntity && !blockEntity.isRemoved() && blockEntity.hasWorld()) {
						((DeviceEntity) blockEntity).sendAllData();
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}

