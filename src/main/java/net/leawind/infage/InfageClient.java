package net.leawind.infage;

import net.fabricmc.api.ClientModInitializer;

public class InfageClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.out.println("================================================");
		System.out.println("InfageClient: I'm here!!!");
		System.out.println("================================================");
	}
}
