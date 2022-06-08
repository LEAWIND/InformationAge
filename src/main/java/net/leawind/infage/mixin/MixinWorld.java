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
import net.leawind.infage.script.ScriptHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

// TODO Mult
@Mixin(World.class)
public class MixinWorld {
	@Shadow
	public final List<BlockEntity> tickingBlockEntities = Lists.newArrayList();

	@Inject(at = @At("HEAD"), method = "tickBlockEntities()V")
	private void onTickBlockEntities_head(CallbackInfo info) {
		// System.out.println("BlockEntities count = " + tickingBlockEntities.size()); // 打印当前方块实体数量
		// 遍历方块实体并更新
		Iterator<BlockEntity> iterator = tickingBlockEntities.iterator(); // 获取世界中方块实体的迭代器
		while (iterator.hasNext()) {
			BlockEntity blockEntity = (BlockEntity) iterator.next(); // 向迭代器获取下一个 方块实体
			if (blockEntity instanceof DeviceEntity && !blockEntity.isRemoved() && blockEntity.hasWorld()) {
				// 如果 这个实体是设备方块 且 没有被移除 且 存在于世界中
				DeviceEntity deviceEntity = (DeviceEntity) blockEntity;
				for (int i = 0; i < deviceEntity.portsCount; i++) {
					if (deviceEntity.portsStatus[i] >= 0 && deviceEntity.sendCaches[i] != null && deviceEntity.sendCaches[i] != "") {
						// 如果 该端口已启用 且 输出缓存里有内容
						// 那么将 设备该端口中的输出缓存 中的内容写入到 与它相连的那个设备 的 输入缓存 中
						BlockEntity targetEntity = deviceEntity.getConnectedDevice(i);
						if (targetEntity != null && targetEntity instanceof DeviceEntity) {
							// 如果 目标存在 且 是设备方块实体
							((DeviceEntity) targetEntity).receiveCaches[deviceEntity.portsStatus[i]] = deviceEntity.sendCaches[i]; // 写入
							// 在这不应该清空 接收或发送缓存，缓存应该由设备自己 在 tick 中决定何时清空
						} else { // 目标不存在 或者 不是设备方块
							// 断开连接
							deviceEntity.portsStatus[i] = -1;
						}
					}
				}
			}
		}
		// 清空所有 execute 任务
		int kills = ScriptHelper.MTMANGER.clearByWeight((byte) 10, (byte) 11, true); // execute 任务权重是 10
		System.out.println("Kills = " + kills);
	}
}

