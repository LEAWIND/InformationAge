package net.leawind.infage.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.leawind.infage.network.packet.c2s.DeviceUpdateC2SPacket;

public class InfageGlobalReceiver {
	static {
	}

	@Environment(EnvType.CLIENT)
	public static void registerS2CReceivers() {}


	public static void registerC2SReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(DeviceUpdateC2SPacket.PACKET_ID, DeviceUpdateC2SPacket::apply);

	}

}
