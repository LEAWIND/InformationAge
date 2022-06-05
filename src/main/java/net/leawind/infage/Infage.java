// 参考 

// [耗子大佬的博客](https://mouse0w0.github.io/archives/page/2/)
// https://www.bilibili.com/read/readlist/rl433929?spm_id_from=333.999.0.0
// https://fabricmc.net/wiki/zh_cn:start

package net.leawind.infage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.registry.InfageItemGroups;
import net.leawind.infage.registry.InfageItems;
import net.leawind.infage.util.DataEncoding;

public class Infage implements ModInitializer {
	public static final Logger LOGGER;
	// 命名空间
	public static final String NAMESPACE;
	public static final int MAX_TRANSMISSION_UNIT; // 发送缓存大小 = 最大传输单元
	public static final int NASHORN_TIME_LIMIT = 8; // 脚本事件执行时长上限
	static {
		LOGGER = LogManager.getLogger("Infage");
		NAMESPACE = "infage";
		MAX_TRANSMISSION_UNIT = 750;

		DataEncoding.test();
	}

	@Override
	public void onInitialize() {
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
