package net.leawind.infage.registry;
// https://fmltutor.ustc-zzzz.net/3.2.1-BlockState%E5%92%8CMetadata.html


import net.leawind.infage.Infage;
import net.leawind.infage.block.Computer;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.block.Disk;
import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.block.PowerController;
import net.leawind.infage.block.PowerSensor;
import net.leawind.infage.block.Switch;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InfageBlocks {
	public static final DeviceBlock COMPUTER;
	public static final DeviceBlock SWITCH;
	public static final DeviceBlock DISK;
	public static final DeviceBlock ITEM_GENERATOR;
	public static final DeviceBlock ITEM_REGESTER;
	public static final DeviceBlock POWER_CONTROLLER;
	public static final DeviceBlock POWER_SENSOR;

	static {
		// 在这实例化各种方块
		COMPUTER = new Computer();
		SWITCH = new Switch();
		DISK = new Disk();
		ITEM_GENERATOR = new ItemGenerator();
		ITEM_REGESTER = new ItemRegester();
		POWER_CONTROLLER = new PowerController();
		POWER_SENSOR = new PowerSensor();
	}

	public InfageBlocks() {
		// 注册这些方块
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Computer.BLOCK_ID), COMPUTER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Switch.BLOCK_ID), SWITCH);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Disk.BLOCK_ID), DISK);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, ItemGenerator.BLOCK_ID), ITEM_GENERATOR);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, ItemRegester.BLOCK_ID), ITEM_REGESTER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, PowerController.BLOCK_ID), POWER_CONTROLLER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, PowerSensor.BLOCK_ID), POWER_SENSOR);
	}
}
