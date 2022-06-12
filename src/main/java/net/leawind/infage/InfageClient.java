package net.leawind.infage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class InfageClient implements ClientModInitializer {
	private static final Logger LOGGER;
	static {
		LOGGER = LogManager.getLogger("InfageClient");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("InfageClient: I'm here!!!");

	}
}
