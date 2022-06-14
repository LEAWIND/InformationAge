package net.leawind.infage.network.packet.c2s;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.leawind.infage.network.AbstractInfagePacket;

public abstract class AbstractC2SPacket extends AbstractInfagePacket {
	/**
	 * 在客户端将这个包发送到服务器
	 */
	public void send() {
		ClientPlayNetworking.send(this.getId(), this);
	}

}
