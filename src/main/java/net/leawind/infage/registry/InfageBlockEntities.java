package net.leawind.infage.registry;

import net.leawind.infage.Infage;
import net.leawind.infage.block.Computer;
import net.leawind.infage.block.Disk;
import net.leawind.infage.block.ItemGenerator;
import net.leawind.infage.block.ItemRegester;
import net.leawind.infage.block.PowerController;
import net.leawind.infage.block.PowerSensor;
import net.leawind.infage.block.Switch;
import net.leawind.infage.blockentity.ComputerEntity;
import net.leawind.infage.blockentity.DiskEntity;
import net.leawind.infage.blockentity.ItemGeneratorEntity;
import net.leawind.infage.blockentity.ItemRegesterEntity;
import net.leawind.infage.blockentity.PowerControllerEntity;
import net.leawind.infage.blockentity.PowerSensorEntity;
import net.leawind.infage.blockentity.SwitchEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InfageBlockEntities {
	// 定义各种方块实体类型
	public static BlockEntityType<ComputerEntity> COMPUTER;
	public static BlockEntityType<DiskEntity> DISK;
	public static BlockEntityType<ItemGeneratorEntity> ITEM_GENERATOR;
	public static BlockEntityType<ItemRegesterEntity> ITEM_REGESTER;
	public static BlockEntityType<PowerControllerEntity> POWER_CONTROLLER;
	public static BlockEntityType<PowerSensorEntity> POWER_SENSOR;
	public static BlockEntityType<SwitchEntity> SWITCH;

	public InfageBlockEntities() {
		// 注册方块实体也需要 id
		// 这后缀会加在方块 id 后面, 得到方块实体 id
		final String ID_SUFFIX = "_blockentity";
		// 注册方块实体
		COMPUTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, Computer.BLOCK_ID + ID_SUFFIX),
				// 需要在那个 方块实体类 中定义无参构造
				BlockEntityType.Builder.create(ComputerEntity::new, InfageBlocks.COMPUTER).build(null));
		DISK = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, Disk.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(DiskEntity::new, InfageBlocks.DISK).build(null));
		ITEM_GENERATOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, ItemGenerator.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(ItemGeneratorEntity::new, InfageBlocks.ITEM_GENERATOR).build(null));
		ITEM_REGESTER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, ItemRegester.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(ItemRegesterEntity::new, InfageBlocks.ITEM_REGESTER).build(null));
		POWER_CONTROLLER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, PowerController.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(PowerControllerEntity::new, InfageBlocks.POWER_CONTROLLER).build(null));
		POWER_SENSOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, PowerSensor.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(PowerSensorEntity::new, InfageBlocks.POWER_SENSOR).build(null));
		SWITCH = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Infage.NAMESPACE, Switch.BLOCK_ID + ID_SUFFIX), BlockEntityType.Builder.create(SwitchEntity::new, InfageBlocks.SWITCH).build(null));
	}
}
