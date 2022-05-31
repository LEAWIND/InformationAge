// 定义了一个物品组，并将一些方块加入了这个组里
// [https://fabricmc.net/wiki/zh_cn:tutorial:itemgroup]

package net.leawind.infage.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.leawind.infage.Infage;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InfageItemGroups {
	public static ItemGroup GROUP_IT = FabricItemGroupBuilder.create(
			new Identifier(Infage.NAMESPACE, "devices"))
			.icon(() -> new ItemStack(InfageBlocks.COMPUTER)) // 物品组图标，会显示在创造模式物品栏
			.appendItems(stacks -> {
				// 在这个匿名函数中向这个物品组里加入物品
				// 这里可以是方块实例，也可以是方块对应的物品实例，效果一样
				stacks.add(new ItemStack(InfageItems.COMPUTER)); // 物品对象此时已经创建完成了
				stacks.add(new ItemStack(InfageItems.SWITCH));
				stacks.add(new ItemStack(InfageItems.DISK));
				stacks.add(new ItemStack(InfageItems.ITEM_GENERATOR));
				stacks.add(new ItemStack(InfageItems.ITEM_REGESTER));
				stacks.add(new ItemStack(InfageItems.POWER_CONTROLER));
				stacks.add(new ItemStack(InfageItems.POWER_SENSOR));
			}).build();
}
