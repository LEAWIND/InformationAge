package net.leawind.infage.client.gui.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbstractButtonWidget extends ButtonWidget {
	public static final Identifier ICONS_LOCATION = new Identifier("textures/gui/icons.png");

	public AbstractButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
		super(x, y, width, height, message, onPress);
	}

	public AbstractButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier) {
		super(x, y, width, height, message, onPress, tooltipSupplier);
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
