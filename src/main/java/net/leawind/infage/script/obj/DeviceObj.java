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
	public String outputs = "";

	public DeviceObj(DeviceEntity that) {
		// 要告知脚本的信息：
		this.storageSize = that.storageSize; // 存储空间大小
		this.storage = Arrays.copyOf(that.storage, that.storageSize); // 存储内容（字节数组）
		this.inData = Arrays.copyOf(that.sendCaches, that.portsCount); // 输入缓存
		this.portStates = new boolean[that.portsCount]; // 各接口的连接状态
		for (int i = 0; i < that.portsCount; i++)
			this.portStates[i] = that.portStates[i] != -128;

		// 脚本可以输出的信息：
		this.dataToSend = new String[this.portStates.length];
		Arrays.fill(this.dataToSend, "");
	}

	// 向控制台输出信息
	public synchronized void println(Object... objs) {
		for (Object obj : objs)
			this.outputs += obj + " ";
		this.outputs += "\n";
	}
	public synchronized void print(Object... objs) {
		for (Object obj : objs)
			this.outputs += obj + " ";
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
