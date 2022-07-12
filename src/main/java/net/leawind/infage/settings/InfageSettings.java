package net.leawind.infage.settings;

import net.leawind.infage.Infage;

public class InfageSettings {
	public static final class Limit {
		public static final int[] PORTS_COUNT = {1, 15};
		public static final int[] STORAGE_SIZE = {8, 8192};
	}
	public static final int AVAILABLE_PROCESSORS; // 处理器可用逻辑核心数

	public static final String NAMESPACE = "infage"; // 命名空间

	public static final int DEVICE_TICK_INTERVAL = 4; // 设备刻间隔 (单位是 方块实体刻 == 游戏刻 == 1/20s == 50ms)
	public static final int MAX_SCRIPT_SIZE = 20479; // 脚本最大字节数
	public static final int TIMEOUT_THREASHOLD = 6; // 脚本超时计数器阈值，超过后禁止再次执行
	public static final int EXEC_THREAD_COUNT; // 执行脚本线程数
	public static final int COMPILE_THREAD_COUNT; // 编译脚本线程数

	public static final int MAX_TRANSMISSION_UNIT = 768; // 发送缓存大小 = 最大传输单元
	public static final int OUTPUTS_SIZE = 2048; // 输出缓冲区最大字符数

	public static final int DEVICE_INVENTORY_SIZE = 4; // 设备库存容量(物品消化器和物品生成器)
	public static int[] AVAILABLE_SLOTS; // 可以 insert 的槽位列表

	static {
		AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

		COMPILE_THREAD_COUNT = (int) (AVAILABLE_PROCESSORS * 0.2 + 1);

		if (Infage.DEBUG_MODE) {
			EXEC_THREAD_COUNT = 3;
		} else {
			EXEC_THREAD_COUNT = (int) (AVAILABLE_PROCESSORS * 0.7 + 1);
		}

		AVAILABLE_SLOTS = new int[DEVICE_INVENTORY_SIZE];
		for (int i = 0; i < DEVICE_INVENTORY_SIZE; i++)
			AVAILABLE_SLOTS[i] = i;
	}
}
