package net.leawind.infage.screen;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.ImplementedInventory;
import net.leawind.infage.registry.InfageScreenHandlers;
import net.leawind.infage.settings.InfageSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfageDeviceScreenHandler extends ScreenHandler {
	public static final Logger LOGGER = LogManager.getLogger("InfageDeviceScreenHandler");
	private Inventory inventory;

	public UUID playerUUID;
	public Text displayName;
	public BlockPos pos;
	public boolean isRunning;
	public int portsCount;
	public byte[] portsStatus;
	public boolean hasItemSlots;
	public ItemStack[] itemsStacks;

	private DeviceEntity deviceEntity;

	// 服务器想要客户端开启 screenHandler 时，客户端调用这个构造器
	// 如有空的物品栏，客户端会调用其他构造器，screenHandler 将会自动
	// 客户端回用一个空的物品栏调用另一个构造器，然后 screenHandler 会自动用服务器端的物品栏同步这个空物品栏
	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory, null, null);
		this.readScreenOpeningData(buf);
	}

	// 此构造函数从服务器上的 BlockEntity 调用，服务器知道容器的库存(你可以将它理解为物品栏)
	// 因此可以直接将其作为参数提供。 然后，此库存(物品栏)将同步到客户端。
	// [DeviceEntity.createMenu()] 服务端
	public InfageDeviceScreenHandler(int syncId, PlayerInventory playerInventory, World world, BlockPos pos) {
		super(InfageScreenHandlers.DEVICE_SCREEN_HANDLER, syncId);
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

	// 从服务端发过来了一段字节流，从中读取本方块实体的数据
	public void readScreenOpeningData(PacketByteBuf buf) {
		this.playerUUID = buf.readUuid();
		this.displayName = buf.readText();
		this.pos = buf.readBlockPos();
		this.isRunning = buf.readBoolean();
		this.portsCount = buf.readByte();
		this.portsStatus = buf.readByteArray();
		this.hasItemSlots = buf.readBoolean();
		if (this.hasItemSlots) {
			this.itemsStacks = new ItemStack[InfageSettings.DEVICE_INVENTORY_SIZE];
			for (int i = 0; i < InfageSettings.DEVICE_INVENTORY_SIZE; i++)
				this.itemsStacks[i] = buf.readItemStack();
		}
	}
}
