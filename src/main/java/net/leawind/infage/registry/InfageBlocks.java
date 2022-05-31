package net.leawind.infage.registry;

import net.leawind.infage.Infage;
import net.leawind.infage.block.Computer;
import net.leawind.infage.block.DeviceBlock;
import net.leawind.infage.block.Disk;
import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.block.PowerControler;
import net.leawind.infage.block.PowerSensor;
import net.leawind.infage.block.Switch;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InfageBlocks {
	// 在这实例化各种方块
	public static final DeviceBlock COMPUTER = new Computer();
	public static final DeviceBlock SWITCH = new Switch();
	public static final DeviceBlock DISK = new Disk();
	public static final DeviceBlock ITEM_GENERATOR = new ItemGenerator();
	public static final DeviceBlock ITEM_REGESTER = new ItemRegester();
	public static final DeviceBlock POWER_CONTROLER = new PowerControler();
	public static final DeviceBlock POWER_SENSOR = new PowerSensor();

	public static final void register() {
		// 注册这些方块
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Computer.BLOCK_ID), COMPUTER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Switch.BLOCK_ID), SWITCH);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, Disk.BLOCK_ID), DISK);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, ItemGenerator.BLOCK_ID), ITEM_GENERATOR);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, ItemRegester.BLOCK_ID), ITEM_REGESTER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, PowerControler.BLOCK_ID), POWER_CONTROLER);
		Registry.register(Registry.BLOCK, new Identifier(Infage.NAMESPACE, PowerSensor.BLOCK_ID), POWER_SENSOR);
	}
}
