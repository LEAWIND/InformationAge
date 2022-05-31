package net.leawind.infage.mixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.TitleScreen;

@Mixin(TitleScreen.class)
public class InfageMixin {
	private static final Logger LOGGER = LogManager.getLogger("InfageClient");

	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		LOGGER.debug("InfageMixin: I'm here!!!");
	}
}
