
package net.leawind.infage.registry;

import net.leawind.infage.Infage;
import net.leawind.infage.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InfageItems {
	// 创建各个方块对应的物品
	// 以及一些其他的物品（暂无）
	// BlockItem 继承自 Item
	public static final BlockItem COMPUTER = new BlockItem(InfageBlocks.COMPUTER,
			Computer.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem DISK = new BlockItem(InfageBlocks.DISK,
			Disk.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem ITEM_GENERATOR = new BlockItem(InfageBlocks.ITEM_GENERATOR,
			ItemGenerator.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem ITEM_REGESTER = new BlockItem(InfageBlocks.ITEM_REGESTER,
			ItemRegester.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem POWER_CONTROLER = new BlockItem(InfageBlocks.POWER_CONTROLER,
			PowerControler.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem POWER_SENSOR = new BlockItem(InfageBlocks.POWER_SENSOR,
			PowerSensor.DEFAULT_BLOCKITEM_SETTINGS);
	public static final BlockItem SWITCH = new BlockItem(InfageBlocks.SWITCH,
			Switch.DEFAULT_BLOCKITEM_SETTINGS);

	public static final void register() {
		// 注册这些物品
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, Computer.BLOCK_ID), COMPUTER);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, Disk.BLOCK_ID), DISK);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, ItemGenerator.BLOCK_ID), ITEM_GENERATOR);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, ItemRegester.BLOCK_ID), ITEM_REGESTER);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, PowerControler.BLOCK_ID), POWER_CONTROLER);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, PowerSensor.BLOCK_ID), POWER_SENSOR);
		Registry.register(Registry.ITEM, new Identifier(Infage.NAMESPACE, Switch.BLOCK_ID), SWITCH);
	}
}
