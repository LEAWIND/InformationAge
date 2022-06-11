package net.leawind.infage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.leawind.infage.client.gui.screen.InfageDeviceScreen;
import net.leawind.infage.registry.InfageScreenHandlers;

public class InfageClient implements ClientModInitializer {
	private static final Logger LOGGER;
	static {
		LOGGER = LogManager.getLogger("InfageClient");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("InfageClient: I'm here!!!");

		// 注册屏幕 (仅限客户端)
		ScreenRegistry.register(InfageScreenHandlers.DEVICE_SCREEN_HANDLER, InfageDeviceScreen::new);

		// 注册屏幕处理器
		new InfageScreenHandlers();
	}
}
