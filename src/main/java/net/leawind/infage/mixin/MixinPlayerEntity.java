// 参考耗子大佬的博客 [https://mouse0w0.github.io/categories/Mixin/]

package net.leawind.infage.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements HasMethodOpenDeviceBlockScreen {
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void openDeviceBlockScreen(DeviceEntity deviceEntity) {
	}
}
