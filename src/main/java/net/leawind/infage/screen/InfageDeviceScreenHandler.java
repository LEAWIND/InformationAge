package net.leawind.infage.screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.ImplementedInventory;
import net.leawind.infage.registry.InfageScreenHandlers;
import net.leawind.infage.settings.InfageSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfageDeviceScreenHandler extends ScreenHandler {
	public static final Logger LOGGER = LogManager.getLogger("InfageDeviceScreenHandler");
	private Inventory inventory;
	private DeviceEntity deviceEntity;
	private BlockPos pos;
	private World world;

	// 服务器想要客户端开启 screenHandler 时，客户端调用这个构造器
	// 如有空的物品栏，客户端会调用其他构造器，screenHandler 将会自动
	// 客户端回用一个空的物品栏调用另一个构造器，然后 screenHandler 会自动用服务器端的物品栏同步这个空物品栏
	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, null, null);
	}

	// 这个构造器是在服务器的 BlockEntity 中被调用的，无需线调用其他构造器，服务器知道容器的物品栏
	// 并直接将其作为参数传入。然后物品栏在客户端完成同步
	// 这个构造器由服务器在 BlockEntity 调用，不需要先调用另一个构造器
	// 因为服务器知道容器的数据，所以可以直接将它作为参数提供
	// 然后这个物品栏会同步到客户端
	// [DeviceEntity.createMenu()] 在服务端调用
	public InfageDeviceScreenHandler(int syncId, World world, BlockPos pos) {
		super(InfageScreenHandlers.DEVICE_SCREEN_HANDLER, syncId);
		this.world = world;
		this.pos = pos;
		if (this.deviceEntity instanceof ImplementedInventory) {
			checkSize(this.inventory, InfageSettings.DEVICE_INVENTORY_SIZE);
			// 这些 slots 物品槽 在服务端和客户端都存在！
			// 这并不会绘制物品槽背景，那是 Screen 类的工作
			for (int i = 0; i < InfageSettings.DEVICE_INVENTORY_SIZE; i++)
				this.addSlot(new Slot(inventory, i, i * 18, i * 18));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public BlockPos getBlockPos() {
		return this.pos;
	}

	public World getWorld() {
		return this.world;
	}
}
