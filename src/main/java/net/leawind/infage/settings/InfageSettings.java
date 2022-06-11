package net.leawind.infage.settings;

public class InfageSettings {
	public static final int AVAILABLE_PROCESSORS; // 处理器可用逻辑核心数

	public static final String NAMESPACE; // 命名空间

	public static final int EXEC_THREAD_COUNT; // 执行脚本线程数
	public static final int COMPILE_THREAD_COUNT; // 编译脚本线程数

	public static final int MAX_TRANSMISSION_UNIT; // 发送缓存大小 = 最大传输单元
	public static final int OUTPUTS_SIZE; // 输出缓冲区最大字符数

	public static final int DEVICE_INVENTORY_SIZE; // 设备库存容量(物品消化器和物品生成器)
	public static int[] AVAILABLE_SLOTS; // 可以 insert 的槽位列表

	static {
		NAMESPACE = "infage";
		AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
		EXEC_THREAD_COUNT = (int) (AVAILABLE_PROCESSORS * 0.7 + 1);
		COMPILE_THREAD_COUNT = (int) (AVAILABLE_PROCESSORS * 0.2 + 1);

		MAX_TRANSMISSION_UNIT = 768;
		OUTPUTS_SIZE = 2048;
		DEVICE_INVENTORY_SIZE = 4;

		AVAILABLE_SLOTS = new int[DEVICE_INVENTORY_SIZE];
		for (int i = 0; i < DEVICE_INVENTORY_SIZE; i++)
			AVAILABLE_SLOTS[i] = i;
	}
}
