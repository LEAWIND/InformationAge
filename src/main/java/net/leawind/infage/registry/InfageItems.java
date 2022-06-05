
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
	public static final BlockItem COMPUTER;
	public static final BlockItem DISK;
	public static final BlockItem ITEM_GENERATOR;
	public static final BlockItem ITEM_REGESTER;
	public static final BlockItem POWER_CONTROLER;
	public static final BlockItem POWER_SENSOR;
	public static final BlockItem SWITCH;

	static {
		COMPUTER = new BlockItem(InfageBlocks.COMPUTER, Computer.getDefaultBlockItemSettings());
		DISK = new BlockItem(InfageBlocks.DISK, Disk.getDefaultBlockItemSettings());
		ITEM_GENERATOR = new BlockItem(InfageBlocks.ITEM_GENERATOR, ItemGenerator.getDefaultBlockItemSettings());
		ITEM_REGESTER = new BlockItem(InfageBlocks.ITEM_REGESTER, ItemRegester.getDefaultBlockItemSettings());
		POWER_CONTROLER = new BlockItem(InfageBlocks.POWER_CONTROLER, PowerControler.getDefaultBlockItemSettings());
		POWER_SENSOR = new BlockItem(InfageBlocks.POWER_SENSOR, PowerSensor.getDefaultBlockItemSettings());
		SWITCH = new BlockItem(InfageBlocks.SWITCH, Switch.getDefaultBlockItemSettings());
	}

	public InfageItems() {
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
