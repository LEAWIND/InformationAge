package net.leawind.infage.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.screen.InfageDeviceScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class InfageScreenHandlers {
	public static final ScreenHandlerType<InfageDeviceScreenHandler> DEVICE_SCREEN_HANDLER;
	static {
		// https://fabricmc.net/wiki/tutorial:screenhandler?rev=1613658170
		DEVICE_SCREEN_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(DeviceBlock.BLOCK_ID), InfageDeviceScreenHandler::new);
	}
}
