package net.leawind.infage.client.gui.screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends HandledScreen<ScreenHandler> {
	private static final Logger LOGGER;
	private static final Identifier TEXTURE;

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
		TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");
	}

	public InfageDeviceScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	// 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init();
		System.out.println("Infage Device Screen: init");
		titleX = (int) (width * 0.25);
		titleY = (int) (height * 0.0);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		client.getTextureManager().bindTexture(TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}

	// 渲染界面
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}


}
