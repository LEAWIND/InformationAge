package net.leawind.infage.screen;

import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.ImplementedInventory;
import net.leawind.infage.registry.InfageScreenHandlers;
import net.leawind.infage.settings.InfageSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class InfageDeviceScreenHandler extends ScreenHandler {
	private Inventory inventory;
	public DeviceEntity deviceEntity;

	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory, DeviceEntity deviceEntity) {
		this(syncId, playerInventory);
		this.deviceEntity = deviceEntity;
	}

	// 服务器想要客户端开启 screenHandler 时，客户端调用这个构造器。
	// 如有空的物品栏，客户端会调用其他构造器，screenHandler 将会自动
	// 在客户端将空白物品栏同步给物品栏。
	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(9));
	}

	// 这个构造器是在服务器的 BlockEntity 中被调用的，无需线调用其他构造器，服务器知道容器的物品栏
	// 并直接将其作为参数传入。然后物品栏在客户端完成同步。
	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(InfageScreenHandlers.DEVICE_SCREEN_HANDLER, syncId);
		this.inventory = inventory;
		// 玩家开启时，一些物品栏有自定义的逻辑。
		inventory.onOpen(playerInventory.player);
		if (this.deviceEntity instanceof ImplementedInventory) {
			checkSize(inventory, InfageSettings.DEVICE_INVENTORY_SIZE);
			for (int i = 0; i < InfageSettings.DEVICE_INVENTORY_SIZE; i++)
				this.addSlot(new Slot(inventory, i, 80, 17 + i * 18));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
}
