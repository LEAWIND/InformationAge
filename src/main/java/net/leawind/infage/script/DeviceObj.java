package net.leawind.infage.script;

import java.util.Arrays;
import net.leawind.infage.blockentity.DeviceEntity;

public class DeviceObj {

	public static final String type = "device";
	public int storageSize;
	public byte[] storage;
	public boolean[] portsStatus;
	public String[] inData;
	public String[] dataToSend;
	public String outputs;

	public DeviceObj(DeviceEntity that) {
		this.storageSize = that.storageSize;
		this.storage = Arrays.copyOf(that.storage, that.storageSize);
		this.portsStatus = new boolean[that.portsCount];
		this.inData = Arrays.copyOf(that.sendCaches, that.portsCount);

		for (int i = 0; i < that.portsCount; i++)
			this.portsStatus[i] = that.portsStatus[i] >= 0;
	}

	// 输出信息
	public synchronized void print(Object... objs) {
		for (Object obj : objs)
			this.outputs += obj + " ";
		this.outputs += "\n";
	}

	public byte[] getBytes(String str) {
		return str.getBytes();
	}

	public String getStr(byte[] bytes) {
		return new String(bytes);
	}
}
