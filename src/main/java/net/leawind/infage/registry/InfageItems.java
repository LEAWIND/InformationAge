package net.leawind.infage.registry;

import net.leawind.infage.block.Computer;
import net.leawind.infage.block.Disk;
import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.block.PowerController;
import net.leawind.infage.block.PowerSensor;
import net.leawind.infage.block.Switch;
import net.leawind.infage.settings.InfageSettings;
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
	public static final BlockItem POWER_CONTROLLER;
	public static final BlockItem POWER_SENSOR;
	public static final BlockItem SWITCH;

	static {
		COMPUTER = new BlockItem(InfageBlocks.COMPUTER, Computer.getDefaultBlockItemSettings());
		DISK = new BlockItem(InfageBlocks.DISK, Disk.getDefaultBlockItemSettings());
		ITEM_GENERATOR = new BlockItem(InfageBlocks.ITEM_GENERATOR, ItemGenerator.getDefaultBlockItemSettings());
		ITEM_REGESTER = new BlockItem(InfageBlocks.ITEM_REGESTER, ItemRegester.getDefaultBlockItemSettings());
		POWER_CONTROLLER = new BlockItem(InfageBlocks.POWER_CONTROLLER, PowerController.getDefaultBlockItemSettings());
		POWER_SENSOR = new BlockItem(InfageBlocks.POWER_SENSOR, PowerSensor.getDefaultBlockItemSettings());
		SWITCH = new BlockItem(InfageBlocks.SWITCH, Switch.getDefaultBlockItemSettings());
	}

	public InfageItems() {
		// 注册这些物品
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, Computer.BLOCK_ID), COMPUTER);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, Disk.BLOCK_ID), DISK);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, ItemGenerator.BLOCK_ID), ITEM_GENERATOR);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, ItemRegester.BLOCK_ID), ITEM_REGESTER);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, PowerController.BLOCK_ID), POWER_CONTROLLER);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, PowerSensor.BLOCK_ID), POWER_SENSOR);
		Registry.register(Registry.ITEM, new Identifier(InfageSettings.NAMESPACE, Switch.BLOCK_ID), SWITCH);
	}
}
