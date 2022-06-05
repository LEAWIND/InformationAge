package net.leawind.infage.scriptRunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class ScriptRunner {
	private static final Logger LOGGER = LogManager.getLogger("ScriptRunner");

	public static final void evalSync(World world, BlockPos pos, String script) {
		LOGGER.info("eval:", world, pos, script);
	}
}
