

package net.leawind.infage.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.leawind.infage.Infage;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InfageItemGroups {
	// 参考 [https://fabricmc.net/wiki/zh_cn:tutorial:itemgroup]
	// 定义一个物品组
	public static ItemGroup GROUP_IT;
	static {
		// 将一些方块加入这个组里
		GROUP_IT = FabricItemGroupBuilder.create(new Identifier(Infage.NAMESPACE, "devices")) // device 是物品组的名称
				.icon(() -> new ItemStack(InfageBlocks.COMPUTER)) // 通过一个方块实例来定义物品组图标，会显示在创造模式物品栏。
				.appendItems(stacks -> {
					// 在这个匿名函数中向这个物品组里加入物品
					// 这里可以是方块实例，也可以是方块对应的物品实例，效果一样
					// stacks.add(ItemStack.EMPTY); // 这个代表空一格
					stacks.add(new ItemStack(InfageItems.COMPUTER));
					stacks.add(new ItemStack(InfageItems.SWITCH));
					stacks.add(new ItemStack(InfageItems.DISK));
					stacks.add(new ItemStack(InfageItems.ITEM_GENERATOR));
					stacks.add(new ItemStack(InfageItems.ITEM_REGESTER));
					stacks.add(new ItemStack(InfageItems.POWER_CONTROLER));
					stacks.add(new ItemStack(InfageItems.POWER_SENSOR));
				}).build();
	}
}
