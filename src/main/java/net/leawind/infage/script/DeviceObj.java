package net.leawind.infage.script;

import java.util.Arrays;
import net.leawind.infage.blockentity.DeviceEntity;

public class DeviceObj {
	public static final String type = "device";
	public int storageSize;
	public String storage;
	public boolean[] portsStatus;
	public String[] inData;
	public String[] dataToSend;
	public String outputs;

	public DeviceObj(DeviceEntity blockEntity) {
		this.storageSize = blockEntity.storageSize;
		this.storage = blockEntity.storage;
		this.portsStatus = new boolean[blockEntity.portsCount];
		this.inData = Arrays.copyOf(blockEntity.sendCaches, blockEntity.portsCount);
		for (int i = 0; i < blockEntity.portsCount; i++) {
			this.portsStatus[i] = blockEntity.portsStatus[i] >= 0;
		}
	}

	public synchronized void print(Object... objs) {
		for (Object obj : objs)
			this.outputs += obj + " ";
		this.outputs += "\n";
	}
}
