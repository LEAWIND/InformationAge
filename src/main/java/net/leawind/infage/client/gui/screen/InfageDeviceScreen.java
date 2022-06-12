package net.leawind.infage.client.gui.screen;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.systems.RenderSystem;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.client.gui.widget.MultilineTextFieldWidget;
import net.leawind.infage.screen.InfageDeviceScreenHandler;
import net.leawind.infage.settings.InfageStyle;
import net.leawind.infage.settings.InfageTexts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends HandledScreen<ScreenHandler> {
	public static final Logger LOGGER;
	private static final Identifier TEXTURE_WIDGETS;
	InfageDeviceScreenHandler handler;
	private UUID playerUUID; // 玩家 uuid
	private Text displayName; // 显示的设备名称
	private BlockPos pos; // 设备方块位置
	private boolean isRunning = false;
	private int portsCount; // 接口数量
	private byte[] portsStatus; // 接口状态
	private boolean hasItemSlots = false; // 是否有物品槽
	private ItemStack[] itemsStacks; // 物品槽们

	private TextFieldWidget codeField; // 代码域
	private TextFieldWidget outputsField; // 输出域
	public ButtonWidget doneButton; // 完成按钮
	public ButtonWidget powerButton; // 电源按钮
	private ButtonWidget[] portsButtons; // 接口按钮们

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
		TEXTURE_WIDGETS = new Identifier("minecraft", "textures/gui/advancements/widgets.png");
	}

	public InfageDeviceScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = (InfageDeviceScreenHandler) handler;
		getAttributesFromHandler(this.handler); // 从 handler 读取方块数据
		this.portsButtons = new ButtonWidget[this.portsCount]; // 初始化接口按钮数组
	}

	private void getAttributesFromHandler(InfageDeviceScreenHandler handler) {
		this.playerUUID = handler.playerUUID;
		this.displayName = handler.displayName;
		this.pos = handler.pos;
		this.isRunning = handler.isRunning;
		this.portsCount = handler.portsCount;
		this.portsStatus = handler.portsStatus;
		this.hasItemSlots = handler.hasItemSlots;
		this.itemsStacks = handler.itemsStacks;
	}


	@Override
	public void tick() {
		this.codeField.tick();
		// this.outputsField.tick();
	}

	// 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init();
		titleX = (int) (this.width * InfageStyle.title[0]);
		titleY = (int) (this.height * InfageStyle.title[1]);
		// 代码域
		{
			this.codeField = new MultilineTextFieldWidget(this.textRenderer, //
					(int) (this.width * InfageStyle.code[0]), //
					(int) (this.height * InfageStyle.code[1]), //
					(int) (this.width * InfageStyle.code[2]), //
					(int) (this.height * InfageStyle.code[3]), //
					new TranslatableText("itemGroup.infage.devices"));
			this.codeField.setMaxLength(64);
			this.codeField.setText("Code Field");
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
			this.outputsField.setMaxLength(64);
			this.outputsField.setText("outpusField");
			this.outputsField.setEditable(false);
			this.children.add(this.outputsField);
		}
		// 完成按钮
		this.doneButton = (ButtonWidget) this.addButton(new ButtonWidget(//
				(int) (this.width * InfageStyle.done[0]), //
				(int) (this.height * InfageStyle.done[1]), //
				(int) (this.width * InfageStyle.done[2]), //
				(int) (this.height * InfageStyle.done[3]), //
				ScreenTexts.DONE, (buttonWidget) -> {
					LOGGER.info("Clicked done button.");
					this.done();
				}));

		// 电源按钮
		this.powerButton = (ButtonWidget) this.addButton(new ButtonWidget(//
				(int) (this.width * InfageStyle.power[0]), //
				(int) (this.height * InfageStyle.power[1]), //
				(int) (this.width * InfageStyle.power[2]), //
				(int) (this.height * InfageStyle.power[3]), //
				this.isRunning ? InfageTexts.SHUT_DOWN : InfageTexts.BOOT, //
				(buttonWidget) -> {
					LOGGER.info("Clicked power button.");
					this.onTogglePower();
				}));

		// TODO 可能存在的物品槽
		if (this.hasItemSlots) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					
				}
			}
		}
		// TODO 端口按钮们
		for (int i = 0; i < this.portsCount; i++) {
			ButtonWidget portButton = (ButtonWidget) this.addButton(new ButtonWidget(//
					this.width - (int) (this.width * InfageStyle.ports[0]), // x
					this.height - (int) (this.height * InfageStyle.ports[1] * (i + 1)), // y
					(int) (this.width * InfageStyle.ports[0]), // w
					(int) (this.height * InfageStyle.ports[1]), // h
					this.isRunning ? InfageTexts.SHUT_DOWN : InfageTexts.BOOT, //
					(buttonWidget) -> {
						LOGGER.info("Clicked port button ");
						for (int j = 0; j < this.portsButtons.length; j++) {
							if (this.portsButtons[j] == buttonWidget) {
								this.onClickPortButton(j);
								break;
							}
						}
					}));
			portButton.setMessage(new LiteralText("P " + i + "    "));
			this.portsButtons[i] = portButton;
		}
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
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		// 代码文本域
		drawBorder(matrices, //
				(int) (InfageStyle.code[0] * width), //
				(int) (InfageStyle.code[1] * height), //
				(int) (InfageStyle.code[2] * width), //
				(int) (InfageStyle.code[3] * height), //
				2, true//
		);
		// 输出文本域
		drawBorder(matrices, //
				(int) (InfageStyle.outputs[0] * width), //
				(int) (InfageStyle.outputs[1] * height), //
				(int) (InfageStyle.outputs[2] * width), //
				(int) (InfageStyle.outputs[3] * height), //
				2, true//
		);
		// 电源按钮
		drawBorder(matrices, //
				(int) (InfageStyle.power[0] * width), //
				(int) (InfageStyle.power[1] * height), //
				(int) (InfageStyle.power[2] * width), //
				(int) (InfageStyle.power[3] * height), //
				2, true//
		);
		// 完成按钮
		drawBorder(matrices, //
				(int) (InfageStyle.done[0] * width), //
				(int) (InfageStyle.done[1] * height), //
				(int) (InfageStyle.done[2] * width), //
				(int) (InfageStyle.done[3] * height), //
				2, true//
		);
	}

	// 渲染界面
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.codeField.render(matrices, mouseX, mouseY, delta);
		this.outputsField.render(matrices, mouseX, mouseY, delta);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	// 更新
	private boolean act(DeviceEntity.Action action, int... args) {
		// TODO 发送数据包给服务器
		switch (action) {
			case GET_DATA:
				break;
			case BOOT:
			case SHUT_DOWN:
				break;
			case DISCONNECT:
				break;
			case CONNECT:
				break;
			case UPDATE_DATA:
				break;
			case UPDATE_SCRIPT:
				break;
		}
		// this.client.getNetworkHandler().sendPacket(new UpdateDeviceC2SPacket(action));
		return true;
	}

	// 完成，更新数据并退出
	private void done() {
		this.act(DeviceEntity.Action.UPDATE_DATA);
		this.client.openScreen((Screen) null);
	}

	// 按下电源键
	private void onTogglePower() {
		if (this.isRunning) {
			this.act(DeviceEntity.Action.SHUT_DOWN);
		} else {
			this.act(DeviceEntity.Action.BOOT);
		}
		this.isRunning = !this.isRunning;
	}

	// TODO 按下接口按钮
	private void onClickPortButton(int j) {
		LOGGER.info("Port button clicked: " + j);
		if (this.portsStatus[j] < 0) {
			this.act(DeviceEntity.Action.CONNECT, j);
		} else {
			this.act(DeviceEntity.Action.DISCONNECT);
			this.portsStatus[j] = -1;
		}
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
