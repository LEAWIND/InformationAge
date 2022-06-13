package net.leawind.infage.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class InfageTexts {
	public static final Text INFAGE_DEVICES = new TranslatableText("itemGroup.infage.devices");
	public static final Text SHUT_DOWN = new TranslatableText("infage.devicegui.shutdown");

	public static final Text CONSOLE = new TranslatableText("infage.devicegui.console");
	public static final Text PORT = new TranslatableText("infage.devicegui.port");
	public static final Text SHUTDOWN = new TranslatableText("infage.devicegui.shutdown");
	public static final Text BOOT = new TranslatableText("infage.devicegui.boot");

}
