// https://www.bilibili.com/read/readlist/rl433929?spm_id_from=333.999.0.0
// https://www.bilibili.com/read/cv11964630/?from=readlist

package net.leawind.infage;

import net.fabricmc.api.ModInitializer;
import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.registry.InfageItemGroups;
import net.leawind.infage.registry.InfageItems;

public class Infage implements ModInitializer {
	public static final String MOD_ID = "infage";
	public static final String NAMESPACE = "infage"; // 命名空间

	@Override
	public void onInitialize() {
		System.out.println("================================================");
		System.out.println("Infage: LEAWIND HERE!!!");
		System.out.println("================================================");

		InfageBlocks.register(); // 实例化并注册方块
		InfageItems.register(); // 实例化并注册物品
		new InfageItemGroups(); // 创建物品组并加入那些物品
		new InfageBlockEntities(); // 注册方块实体
	}
}
