// 参考

// [yarn](https://maven.fabricmc.net/docs/yarn-1.16.5+build.6/)
// [Fabric API](https://maven.fabricmc.net/docs/fabric-api-0.32.5+1.16/)
// [Fabric Loader](https://maven.fabricmc.net/docs/fabric-loader-0.11.3/)

// [耗子的博客](https://mouse0w0.github.io/archives/page/2/)
// [Fabric Document]()
// https://www.bilibili.com/read/readlist/rl433929?spm_id_from=333.999.0.0
// https://fabricmc.net/wiki/zh_cn:start
// [GUI 界面](https://fabricmc.net/wiki/zh_cn:tutorial:screenhandler)
// [在方块中存储物品](https://fabricmc.net/wiki/tutorial:inventory)

package net.leawind.infage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.registry.InfageItemGroups;
import net.leawind.infage.registry.InfageItems;
import net.leawind.infage.script.ScriptHelper;

public class Infage implements ModInitializer {
	public static final Logger LOGGER;
	public static final String NAMESPACE;
	public static final int MAX_TRANSMISSION_UNIT; // 发送缓存大小 = 最大传输单元
	public static final int OUTPUTS_SIZE; // 输出缓冲区最大字符数
	public static final int DEVICE_INVENTORY_SIZE; // 设备库存容量
	public static int[] AVAILABLE_SLOTS;

	static {
		LOGGER = LogManager.getLogger("Infage");
		NAMESPACE = "infage";
		MAX_TRANSMISSION_UNIT = 768;
		OUTPUTS_SIZE = 2048;

		DEVICE_INVENTORY_SIZE = 4;
		// 任何方向都可以插入所有槽位
		AVAILABLE_SLOTS = new int[Infage.DEVICE_INVENTORY_SIZE];
		for (int i = 0; i < Infage.DEVICE_INVENTORY_SIZE; i++)
			AVAILABLE_SLOTS[i] = i;
	}

	@Override
	public void onInitialize() {
		new ScriptHelper();
		LOGGER.info("Infage.java: I'm here!!!");

		// 实例化并注册方块
		// 注册了的方块才能在世界中被放置
		new InfageBlocks();

		// 实例化并注册物品
		// 注册了的物品才可能出现在玩家物品栏中
		new InfageItems();

		// 创建新的物品组并加入那些物品
		// 物品组会显示在创造模式物品栏里
		new InfageItemGroups();

		// 注册方块实体
		// 方块有了对应的方块实体才能储存数据
		new InfageBlockEntities();
	}
}
