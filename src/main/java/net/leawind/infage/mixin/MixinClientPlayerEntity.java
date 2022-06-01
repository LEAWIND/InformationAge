package net.leawind.infage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.screen.InfageDeviceScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements HasMethodOpenDeviceBlockScreen {

	@Shadow
	protected final MinecraftClient client = null; // TODO 客户端玩家实例 对应的 客户端实例

	@Override
	public void openDeviceBlockScreen(DeviceEntity deviceEntity) {
		this.client.openScreen(new InfageDeviceScreen(deviceEntity));
	}

}
