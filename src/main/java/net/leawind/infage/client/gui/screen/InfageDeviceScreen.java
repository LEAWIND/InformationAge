package net.leawind.infage.client.gui.screen;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.systems.RenderSystem;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.client.gui.widget.LockButtonWidget;
import net.leawind.infage.client.gui.widget.MultilineTextFieldWidget;
import net.leawind.infage.client.gui.widget.StretchableButtonWidget;
import net.leawind.infage.screenhandler.InfageDeviceScreenHandler;
import net.leawind.infage.settings.InfageSettings;
import net.leawind.infage.settings.InfageStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends HandledScreen<ScreenHandler> {
	public static final Logger LOGGER = LogManager.getLogger("InfageDeviceScreen");;
	private static final Identifier TEXTURE_WIDGETS = new Identifier("minecraft", "textures/gui/advancements/widgets.png");
	InfageDeviceScreenHandler handler;
	private UUID playerUUID; // 玩家 uuid (虽然不知道有什么用)

	private Text displayName; // 显示的设备名称
	private BlockPos pos; // 设备方块位置
	private boolean isRunning = false; // 电源状态
	private String script_tick = ""; // 脚本
	private String consoleOutputs = ""; // 输出
	private int portsCount; // 接口数量
	private byte[] portStates; // 接口状态
	private boolean hasItemSlots = false; // 是否有物品槽
	private ItemStack[] itemsStacks; // 物品槽们

	private TextFieldWidget codeField; // 代码域
	private TextFieldWidget outputsField; // 输出域
	public StretchableButtonWidget doneButton; // 完成按钮
	public StretchableButtonWidget powerButton; // 电源按钮
	private StretchableButtonWidget[] portButtons; // 接口按钮们
	private LockButtonWidget[] portLockButtons; // 接口锁按钮们

	public InfageDeviceScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = (InfageDeviceScreenHandler) handler;
		// this.getAttributesFromHandler(this.handler); // 从 handler 读取方块数据
		this.readScreenOpeningData(this.handler.getBuf()); // 从 handler.packetByteBuf 读取方块数据
		this.portButtons = new StretchableButtonWidget[this.portsCount]; // 初始化接口按钮数组
		this.portLockButtons = new LockButtonWidget[this.portsCount]; // 初始化接口按钮数组
	}

	@Override
	public void tick() {
		this.codeField.tick();
		this.outputsField.tick();
	}

	// 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init();
		// 代码域
		{
			this.codeField = new MultilineTextFieldWidget(this.textRenderer, //
					(int) (this.width * InfageStyle.code[0]), //
					(int) (this.height * InfageStyle.code[1]), //
					(int) (this.width * InfageStyle.code[2]), //
					(int) (this.height * InfageStyle.code[3]), //
					new TranslatableText("itemGroup.infage.devices"));
			this.codeField.setMaxLength(InfageSettings.MAX_SCRIPT_SIZE);
			this.codeField.setText(this.script_tick);
			this.children.add(this.codeField);
		}
		// 输出域
		{
			this.outputsField = new MultilineTextFieldWidget(this.textRenderer, //
					(int) (this.width * InfageStyle.outputs[0]), //
					(int) (this.height * InfageStyle.outputs[1]), //
					(int) (this.width * InfageStyle.outputs[2]), //
					(int) (this.height * InfageStyle.outputs[3]), //
					InfageTexts.INFAGE_DEVICES);
			this.outputsField.setMaxLength(InfageSettings.OUTPUTS_SIZE);
			this.outputsField.setEditable(false);
			this.outputsField.setText(this.consoleOutputs);
			this.children.add(this.outputsField);
		}
		// 完成按钮
		this.doneButton = (StretchableButtonWidget) this.addButton(new StretchableButtonWidget(//
				(int) (this.width * InfageStyle.done[0]), //
				(int) (this.height * InfageStyle.done[1]), //
				(int) (this.width * InfageStyle.done[2]), //
				(int) (this.height * InfageStyle.done[3]), //
				ScreenTexts.DONE, (buttonWidget) -> {
					LOGGER.info("Clicked done button.");
					this.onClickDoneButton();
				}));

		// 电源按钮
		this.powerButton = (StretchableButtonWidget) this.addButton(new StretchableButtonWidget(//
				(int) (this.width * InfageStyle.power[0]), //
				(int) (this.height * InfageStyle.power[1]), //
				(int) (this.width * InfageStyle.power[2]), //
				(int) (this.height * InfageStyle.power[3]), //
				this.isRunning ? InfageTexts.SHUT_DOWN : InfageTexts.BOOT, //
				(buttonWidget) -> {
					LOGGER.info("Clicked power button.");
					LOGGER.info("this.portsCount = " + this.portsCount);
					LOGGER.info("this.isRunning = " + this.isRunning);
					this.onClickPowerButton();
				}));
		this.updatePowerButton();

		// 接口按钮 和 接口锁按钮 们
		for (int i = 0; i < this.portsCount; i++) {
			int x, y, w, h;
			x = this.width - (int) (this.width * InfageStyle.ports[0]);
			y = this.height - (int) (this.height * InfageStyle.ports[1] * (i + 1));
			w = (int) (this.width * InfageStyle.ports[0]);
			h = (int) (this.height * InfageStyle.ports[1]);
			// 接口按钮
			StretchableButtonWidget portButton = (StretchableButtonWidget) this.addButton(new StretchableButtonWidget(//
					x, y, w - h, h, //
					new LiteralText("NU"), //
					(buttonWidget) -> {
						for (int j = 0; j < this.portButtons.length; j++) {
							if (this.portButtons[j] == buttonWidget) {
								this.onClickPortButton(j);
								break;
							}
						}
					}));
			this.portButtons[i] = portButton;
			this.updatePortButton(i);

			// 接口锁按钮
			LockButtonWidget lockButton = (LockButtonWidget) this.addButton(new LockButtonWidget(//
					x + w - h, y, h, h, //
					new LiteralText(""), //
					(buttonWidget) -> {
						LOGGER.info("Clicked port lock button ");
						for (int j = 0; j < this.portButtons.length; j++) {
							if (this.portLockButtons[j] == buttonWidget) {
								this.onClickPortLockButton(j);
								break;
							}
						}
					}));
			this.portLockButtons[i] = lockButton;
			this.updatePortLockButton(i);
		}

		// TODO 可能存在的物品槽
		if (this.hasItemSlots)
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String string0 = this.codeField.getText();
		String string1 = this.outputsField.getText();
		this.init(client, width, height);
		this.codeField.setText(string0);
		this.outputsField.setText(string1);
	}

	// 参考 package net.minecraft.client.gui.DrawableHelper;
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {}

	// 渲染界面
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.codeField.render(matrices, mouseX, mouseY, delta);
		this.outputsField.render(matrices, mouseX, mouseY, delta);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	// TODO TODO 发送数据包给服务器
	private boolean act(DeviceEntity.Action action, int... args) {
		switch (action) {
			case GET_ALL_DATA: // 请求获取最新数据
				LOGGER.info("Request: get all data");
				break;
			case RQ_BOOT: // 启动设
				LOGGER.info("Request: boot");
				break;
			case RQ_SHUT_DOWN: // 关闭设备
				LOGGER.info("Request: shut down");
				break;
			case RQ_DISCONNECT: // 断开指定端口
				LOGGER.info("Request: disconnect port " + args[0]);
				break;
			case RQ_CONNECT: // 玩家希望连接指定端口
				LOGGER.info("Request: connect port " + args[0]);
				break;
			case PUSH_ALL_DATA: // 更新数据
				LOGGER.info("Request: push all data");
				break;
			case PUSH_SCRIPT: // 更新脚本
				LOGGER.info("Request: push script");
				break;
			case DRINK_A_CUP_OF_TEA:
				LOGGER.info("Request: Drink a cup of tea.");
				break;
			case RQ_LOCK_PORT:
				LOGGER.info("Request: Lock port " + args[0]);
				break;
			case RQ_UNLOCK_PORT:
				LOGGER.info("Request: Unlock port " + args[0]);
				break;
		}
		// this.client.getNetworkHandler().sendPacket(new UpdateDeviceC2SPacket(action));
		return true;
	}

	// 将 服务端发过来的一串字节 解析为本方块实体的数据
	public void readScreenOpeningData(PacketByteBuf buf) {
		this.playerUUID = buf.readUuid();
		this.displayName = buf.readText();
		this.pos = buf.readBlockPos();
		this.isRunning = buf.readBoolean();
		this.script_tick = buf.readString();
		this.consoleOutputs = buf.readString();
		this.portsCount = buf.readByte();
		this.portStates = buf.readByteArray();
		this.hasItemSlots = buf.readBoolean();
		if (this.hasItemSlots) {
			this.itemsStacks = new ItemStack[InfageSettings.DEVICE_INVENTORY_SIZE];
			for (int i = 0; i < InfageSettings.DEVICE_INVENTORY_SIZE; i++)
				this.itemsStacks[i] = buf.readItemStack();
		}
	}

	// 完成，更新数据并退出
	private void onClickDoneButton() {
		this.act(DeviceEntity.Action.PUSH_ALL_DATA);
		this.client.openScreen((Screen) null);
	}

	// 按下电源键
	private void onClickPowerButton() {
		if (this.isRunning) {
			this.act(DeviceEntity.Action.RQ_SHUT_DOWN); // 请求关机
		} else {
			this.act(DeviceEntity.Action.RQ_BOOT); // 请求开机
		}
		this.isRunning = !this.isRunning;
		this.updatePowerButton();
	}

	// 按下接口按钮
	private void onClickPortButton(int i) {
		// 如果未连接，则告诉服务器这个玩家想连接。如果已连接但没有锁定，则断开连接。如果已经连接且锁定了，那么什么都不做
		if (this.portStates[i] == -128) { // 未连接
			this.act(DeviceEntity.Action.RQ_CONNECT, i);
		} else if (this.portStates[i] < 0) { // 没锁定
			this.act(DeviceEntity.Action.RQ_DISCONNECT, i);
		} else { // 锁定了
			this.act(DeviceEntity.Action.DRINK_A_CUP_OF_TEA);
		}
		this.updatePortButton(i);
		this.updatePortLockButton(i);
	}

	// 按下接口锁按钮
	private void onClickPortLockButton(int i) {
		// 如果未连接则什么也不做，如果已经连接且已锁定则解除锁定，如果已经连接但没锁定则锁定接口
		if (this.portStates[i] == -128) { // 未连接
			this.act(DeviceEntity.Action.DRINK_A_CUP_OF_TEA);
		} else if (this.portStates[i] < 0) { // 没锁定
			this.act(DeviceEntity.Action.RQ_LOCK_PORT, i);
			this.portStates[i] = (byte) (-1 - this.portStates[i]);
		} else { // 锁定了
			this.act(DeviceEntity.Action.RQ_UNLOCK_PORT, i);
			this.portStates[i] = (byte) (-1 - this.portStates[i]);
		}
		this.updatePortLockButton(i);
	}

	// 更新电源按钮
	private void updatePowerButton() {
		this.powerButton.setMessage(this.isRunning ? InfageTexts.SHUT_DOWN : InfageTexts.BOOT); // 设置文本
	}

	// 更新接口按钮
	private void updatePortButton(int portId) {
		this.portButtons[portId].setMessage(new LiteralText("Port " + portId));
	}

	// 更新接口锁按钮
	private void updatePortLockButton(int i) {
		// 根据接口状态决定激活状态
		this.portLockButtons[i].active = this.portStates[i] != -128; // 当接口连接时，按钮才激活
		this.portLockButtons[i].setLocked(this.portStates[i] >= 0); // 锁定状态
	}

	/**
	 * 绘制一个带边框的矩形区域
	 * 
	 * @param matrices the matrix stack used for rendering
	 * @param x the X coordinate of the rectangle
	 * @param y the Y coordinate of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param scale 缩放
	 * @param doFill 是否填充
	 */
	public void drawBorder(MatrixStack matrices, int x, int y, int w, int h, int scale, boolean doFill) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE_WIDGETS);
		int s = scale * 2;
		drawBorderRect(matrices, x, y, w, h, s, 0, 55, 200, 20, 2, 256, 256, doFill);
	}

	/**
	 * 将贴图中的带边框的矩形区域绘制到屏幕，可以拉伸整个矩形而不拉伸边框
	 * 
	 * @param matrices the matrix stack used for rendering
	 * @param x 屏幕上的矩形位置
	 * @param y 屏幕上的矩形位置
	 * @param w 屏幕上的矩形位置
	 * @param h 屏幕上的矩形位置
	 * @param b 边框在屏幕上的宽度
	 * 
	 * @param u 贴图上的矩形位置
	 * @param v 贴图上的矩形位置
	 * @param rw 贴图上的矩形位置
	 * @param rh 贴图上的矩形位置
	 * @param rb 边框在贴图中的宽度
	 * 
	 * @param tw 贴图分辨率
	 * @param th 贴图分辨率
	 * @param doFill 是否填充中心区域
	 */
	public void drawBorderRect(MatrixStack matrices, int x, int y, int w, int h, int b, int u, int v, int rw, int rh, int rb, int tw, int th, boolean doFill) {
		drawTexture(matrices, x, y, b, b, u, v, rb, rb, tw, th); // 左上
		drawTexture(matrices, x, y + h - b, b, b, u, v + rh - rb, rb, rb, tw, th); // 左下
		drawTexture(matrices, x + w - b, y, b, b, u + rw - rb, v, rb, rb, tw, th); // 右上
		drawTexture(matrices, x + w - b, y + h - b, b, b, u + rw - rb, v + rh - rb, rb, rb, tw, th); // 右下
		if (doFill)
			drawTexture(matrices, x + b, y + b, w - 2 * b, h - 2 * b, u + rb, v + rb, rw - 2 * rb, rh - 2 * rb, tw, th); // 中
		drawTexture(matrices, x + b, y, w - 2 * b, b, u + rb, v, rw - 2 * rb, rb, tw, th); // 上
		drawTexture(matrices, x + b, y + h - b, w - 2 * b, b, u + rb, v + rh - rb, rw - 2 * rb, rb, tw, th); // 下
		drawTexture(matrices, x, y + b, b, h - 2 * b, u, v + rb, rb, rh - 2 * rb, tw, th); // 左
		drawTexture(matrices, x + w - b, y + b, b, h - 2 * b, u + rw - rb, v + rb, rb, rh - 2 * rb, tw, th); // 右
	}

}
