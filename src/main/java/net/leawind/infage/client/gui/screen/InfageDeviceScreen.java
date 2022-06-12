package net.leawind.infage.client.gui.screen;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.systems.RenderSystem;
import net.leawind.infage.screen.InfageDeviceScreenHandler;
import net.leawind.infage.settings.InfageStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends HandledScreen<ScreenHandler> {
	public static final Logger LOGGER;
	private static final Identifier TEXTURE_WIDGETS;
	InfageDeviceScreenHandler handler;
	private UUID playerUUID;
	private Text displayName;
	private BlockPos pos;
	private boolean isRunning = false;
	private int portsCount;
	private byte[] portsStatus;
	private boolean hasItemSlots = false;
	private ItemStack[] itemsStacks;

	private ButtonWidget doneButton; // 完成按钮
	private ButtonWidget powerButton; // 电源按钮

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
		TEXTURE_WIDGETS = new Identifier("minecraft", "textures/gui/advancements/widgets.png");
	}

	public InfageDeviceScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = (InfageDeviceScreenHandler) handler;
		getAttributesFromHandler(this.handler);

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

	// 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init();
		titleX = (int) (this.width * InfageStyle.title[0]);
		titleY = (int) (this.height * InfageStyle.title[1]);
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
				this.isRunning ? ScreenTexts.OFF : ScreenTexts.ON, //
				(buttonWidget) -> {
					LOGGER.info("Clicked power button.");
					LOGGER.info("!!!!!!!!!!this.displayName = " + this.displayName);
					LOGGER.info("!!!!!!!!!!this.pos= " + this.pos);
					LOGGER.info("!!!!!!!!!!this.portCount = " + this.portsCount);
					LOGGER.info("!!!!!!!!!!this.portStatus length= " + this.portsStatus.length);
					LOGGER.info("!!!!!!!!!!this.hasItemSlots = " + this.hasItemSlots);
					this.onTogglePower();
				}));
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		this.init(client, width, height);
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
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}


	private void done() {
		this.client.openScreen((Screen) null);
	}

	private void onTogglePower() {
		if (this.isRunning) {
		} else {
		}
		this.isRunning = !this.isRunning;
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
