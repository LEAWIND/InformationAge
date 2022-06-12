package net.leawind.infage.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.screenhandler.InfageDeviceScreenHandler;
import net.leawind.infage.settings.InfageSettings;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

// 注册 ScreenHandler
public class InfageScreenHandlers {
	public static final ScreenHandlerType<InfageDeviceScreenHandler> DEVICE_SCREEN_HANDLER;
	static {
		// https://fabricmc.net/wiki/tutorial:screenhandler?rev=1613658170
		DEVICE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(InfageSettings.NAMESPACE, DeviceBlock.BLOCK_ID), InfageDeviceScreenHandler::new);
	}
}
