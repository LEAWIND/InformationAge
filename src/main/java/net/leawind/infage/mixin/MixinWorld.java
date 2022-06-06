package net.leawind.infage.mixin;

import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.common.collect.Lists;
import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(World.class)
public class MixinWorld {
	@Shadow
	public final List<BlockEntity> tickingBlockEntities = Lists.newArrayList();;

	@Inject(at = @At("RETURN"), method = "tickBlockEntities()V")
	private void onTickBlockEntities(CallbackInfo info) {
		// 遍历方块实体并更新
		Iterator<BlockEntity> iterator = tickingBlockEntities.iterator(); // 获取世界中 方块实体的迭代器
		while (iterator.hasNext()) {
			BlockEntity blockEntity = (BlockEntity) iterator.next(); // 向迭代器获取下一个 方块实体
			if (blockEntity instanceof DeviceEntity && !blockEntity.isRemoved() && blockEntity.hasWorld()) {
				DeviceEntity deviceEntity = (DeviceEntity) blockEntity;
				World world = deviceEntity.getWorld();
				// 实测可以成功找到实体
				for (int i = 0; i < deviceEntity.portsCount; i++) {
					if (deviceEntity.portsStatus[i] >= 0 && deviceEntity.sendCaches[i] != null) {
						// 如果端口已启用 且 输出缓存里有内容
						// 那么将设备 的输出缓存 中的内容写入到 它对应的那个设备 的 输入缓存 中。
						BlockPos targetPos = new BlockPos(deviceEntity.portsX[i], deviceEntity.portsY[i], deviceEntity.portsZ[i]);
						BlockEntity targetEntity = world.getBlockEntity(targetPos);
						if (targetEntity != null && targetEntity instanceof DeviceEntity) {
							// 如果目标存在且是设备方块实体
							((DeviceEntity) targetEntity).receiveCaches[deviceEntity.portsStatus[i]] = deviceEntity.sendCaches[i];
							deviceEntity.sendCaches[i] = ""; // 清空发送缓存
						} else {
							// 断开连接
							deviceEntity.portsStatus[i] = -1;
						}

					}
				}
			}
		}
	}

}

