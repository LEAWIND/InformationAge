// 参考
// [yarn](https://maven.fabricmc.net/docs/yarn-1.16.5+build.6/)
// [Fabric API](https://maven.fabricmc.net/docs/fabric-api-0.32.5+1.16/)
// [Fabric Loader](https://maven.fabricmc.net/docs/fabric-loader-0.11.3/)
// [耗子的博客](https://mouse0w0.github.io/archives/page/2/)
// https://fabricmc.net/wiki/zh_cn:start
// https://www.bilibili.com/read/readlist/rl433929?spm_id_from=333.999.0.0
// [在方块中存储物品](https://fabricmc.net/wiki/tutorial:inventory)
// [GUI 界面 1.16.5](https://fabricmc.net/wiki/tutorial:screenhandler?rev=1613658170)
// [GUI 界面](https://fabricmc.net/wiki/zh_cn:tutorial:screenhandler)
// [1.16.5 toTag, fromTag](https://fabricmc.net/wiki/tutorial:blockentity?rev=1563817083)
// [网络通信](https://fabricmc.net/wiki/zh_cn:tutorial:networking)


package net.leawind.infage;

import java.util.HashMap;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.registry.InfageGlobalReceiver;
import net.leawind.infage.registry.InfageItemGroups;
import net.leawind.infage.registry.InfageItems;
import net.leawind.infage.registry.InfageScreenHandlers;
import net.leawind.infage.settings.InfageSettings;

public class Infage implements ModInitializer {
	public static final Logger LOGGER;
	public volatile static int blockEntityTickCounter = 0; // 方块实体刻计数器
	private volatile static boolean isDeviceTickNow = false;
	public static boolean DEBUG_MODE = true;

	public static void increaseBlockEntityTickCounter() {
		isDeviceTickNow = ++Infage.blockEntityTickCounter % InfageSettings.DEVICE_TICK_INTERVAL == 0;
	}

	public static boolean isDeviceTickNow() {
		return isDeviceTickNow;
	}

	// 全局变量
	public static HashMap<UUID, PlayerData> players = new HashMap<>();

	static {
		LOGGER = LogManager.getLogger("Infage");

		// 用于执行方块实体脚本
		// new ScriptHelper();

		// 各种参数设置
		new InfageSettings();

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

		// 注册网络数据包监听器
		// 让服务端监听来自客户端的数据包。
		InfageGlobalReceiver.registerC2SReceivers();

		// 注册屏幕 handler
		new InfageScreenHandlers();

	}

	@Override
	public void onInitialize() {
		LOGGER.info("Infage.java: I'm here!!!");
	}

	public static void db_info(Object... objs) {
		if (DEBUG_MODE) {
			String msg = "\033[38;2;255;255;0m[DEBUG-info] \033[38;2;50;50;255m";
			for (Object obj : objs)
				msg += obj + " ";
			msg += "\033[0m";
			synchronized (System.out) {
				System.out.println(msg);
			}
		}
	}

	public static void db_error(Object... objs) {
		if (DEBUG_MODE) {
			String msg = "\033[38;2;255;50;50m[DEBUG-error] \033[38;2;255;255;50m";
			for (Object obj : objs)
				msg += obj + " ";
			msg += "\033[0m";

			synchronized (System.out) {
				System.out.println(msg);
			}
		}
	}
}
