package net.leawind.infage.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class LockButtonWidget extends AbstractButtonWidget {
	private boolean isLocked;

	public LockButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
		this(x, y, width, height, message, onPress, EMPTY);
	}

	public LockButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier) {
		super(x, y, width, height, message, onPress, tooltipSupplier);
	}

	public int getU() {
		if (this.isLocked) {
			return 0;
		} else {
			return 0 + 20;
		}
	}

	public int getV(int i) {
		return 146 + 20 * ((i + 2) % 3);
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		TextRenderer textRenderer = minecraftClient.textRenderer;
		minecraftClient.getTextureManager().bindTexture(WIDGETS_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		int i = this.getYImage(this.isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		this.drawBorderRect(matrices, this.x, this.y, this.width, this.height, 1, this.getU(), this.getV(i), 20, 20, 1, 256, 256, true);
		this.renderBg(matrices, minecraftClient, mouseX, mouseY);
		int j = this.active ? 16777215 : 10526880;
		drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
		if (this.isHovered()) {
			this.renderToolTip(matrices, mouseX, mouseY);
		}
	}


	public void setLocked(boolean x) {
		this.isLocked = x;
	}

	public boolean isLocked() {
		return this.isLocked;
	}


}
