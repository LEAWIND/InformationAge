/*
I'm sorry that
 all code comments in this project are in Chinese,
except this one.
*/
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

// 在 fabric.mod.json 中定义的 main 入口(类)

public class Infage implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("Infage");
	// 命名空间
	public static final String NAMESPACE = "infage";

	@Override
	// 这会在初始化时运行
	public void onInitialize() {
		LOGGER.debug("Infage.java: I'm here!!!");

		// 实例化并注册方块
		// 注册了的方块才能在世界中被放置
		InfageBlocks.register();

		// 实例化并注册物品
		// 注册了的物品才能出现在玩家物品栏中
		InfageItems.register();

		// 创建新的物品组并加入那些物品
		// 物品组会显示在创造模式物品栏里
		new InfageItemGroups();

		// 注册方块实体
		// 方块有了对应的方块实体才能储存数据
		new InfageBlockEntities();
	}
}
