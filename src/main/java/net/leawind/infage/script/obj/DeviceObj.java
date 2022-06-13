package net.leawind.infage.script.obj;

import java.util.Arrays;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.blockentity.DeviceEntity;

public class DeviceObj {

	public static final String type = DeviceBlock.BLOCK_ID;
	public int storageSize;
	public byte[] storage;
	public boolean[] portStates;
	public String[] inData;
	public String[] dataToSend;
	public String outputs;

	public DeviceObj(DeviceEntity that) {
		// 将属性初始化
		this.storageSize = that.storageSize;
		this.storage = Arrays.copyOf(that.storage, that.storageSize);
		this.portStates = new boolean[that.portsCount];
		this.inData = Arrays.copyOf(that.sendCaches, that.portsCount);

		for (int i = 0; i < that.portsCount; i++)
			this.portStates[i] = that.portStates[i] >= 0;
	}

	// 输出信息
	public synchronized void print(Object... objs) {
		for (Object obj : objs)
			this.outputs += obj + " ";
		this.outputs += "\n";
	}

	// 将字符串转换为字节数组
	public byte[] getBytes(String str) {
		return str.getBytes();
	}

	// 将字节转数组换为字符串
	public String getStr(byte[] bytes) {
		return new String(bytes);
	}
}
