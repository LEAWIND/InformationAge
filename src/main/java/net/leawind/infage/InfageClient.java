package net.leawind.infage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class InfageClient implements ClientModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("InfageClient");
	@Override
	public void onInitializeClient() {
		LOGGER.debug("InfageClient: I'm here!!!");
	}
}
