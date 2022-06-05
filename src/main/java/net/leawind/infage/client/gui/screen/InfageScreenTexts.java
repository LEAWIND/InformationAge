package net.leawind.infage.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class InfageScreenTexts {
	public static final Text INIT = new TranslatableText("infage.event.init");
	public static final Text EVENT_BOOT = new TranslatableText("infage.event.boot");
	public static final Text PORT_CONNECT = new TranslatableText("infage.event.port_connect");
	public static final Text PORT_DISCONNECT = new TranslatableText("infage.event.port_disconnect");
	public static final Text RECEIVE_DATA = new TranslatableText("infage.event.receive_data");
	public static final Text POWER_CHANGE = new TranslatableText("infage.event.power_change");
	public static final Text CONSOLE = new TranslatableText("gui.console");
	public static final Text PORT = new TranslatableText("gui.port");
	public static final Text SHUTDOWN = new TranslatableText("gui.shutdown");
	public static final Text BOOT = new TranslatableText("gui.boot");

}
