package net.leawind.infage.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.screen.InfageDeviceScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class InfageScreenHandlers {
	public static final ScreenHandlerType<InfageDeviceScreenHandler> DEVICE_SCREEN_HANDLER;
	static {
		DEVICE_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(DeviceBlock.BLOCK_ID), InfageDeviceScreenHandler::new);
	}
}
