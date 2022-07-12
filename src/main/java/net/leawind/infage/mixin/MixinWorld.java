// Server side only
package net.leawind.infage.mixin;

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
import net.leawind.infage.script.mtt.ExecuteTask;
import net.leawind.universe.mttv3.MTManager;
import net.leawind.universe.mttv3.MTManager.MTState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {

	@Shadow
	public final List<BlockEntity> tickingBlockEntities = Lists.newArrayList();

	@Inject(at = @At("HEAD"), method = "tickBlockEntities()V")
	@SuppressWarnings("deprecation")
	private void onTickBlockEntities_head(CallbackInfo info) {
		Infage.increaseBlockEntityTickCounter(); // increment the tick counter every tickingBlockEntities
		// TODO exec timeout

		// Clear all exec tasks not distributed yet, and get the count of them.
		// Any exec task may time out, so every device entity has a timeout counter.
		// Every time successfully compiled, counter = 0;
		// Every time successfully executed, counter --;
		// The tasks not finished yet are timeout.
		// If a task is not finished by now, it's timeout.
		// their counters += 2;
		// Then force to STOP their threads.
		// If counter is beyond the timeout_threashold, force to STOP their threads as well.
		try {
			if (Infage.isDeviceTickNow()) {
				{
					// Clear all exec tasks not distributed yet, get the count of them.
					int clearedTasksCount = ScriptHelper.MTM_EXEC.clearTasks();
					if (clearedTasksCount > 0)
						Infage.LOGGER.warn(clearedTasksCount + " exec tasks abandoned.\n");
				}
				{
					// Find all exec tasks that timeout
					int stoppedTasksCount = 0;
					for (MTManager.MTThread t : ScriptHelper.MTM_EXEC.threads) {
						if (t.state == MTState.EXECING && t.task != null) {
							((ExecuteTask) t.task).deviceEntity.scriptTimeoutCounter += 2;
							t.stop();
							stoppedTasksCount++;
						}
					}
					if (stoppedTasksCount > 0)
						Infage.LOGGER.warn(stoppedTasksCount + " exec threads still running.\n");
				}
				// When to clear cache should be decided by each device itself.
				for (BlockEntity blockEntity : tickingBlockEntities) {
					if (blockEntity instanceof DeviceEntity && !blockEntity.isRemoved() && blockEntity.hasWorld()) {
						((DeviceEntity) blockEntity).sendAllData();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

