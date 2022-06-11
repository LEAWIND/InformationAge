package net.leawind.infage.client.gui.screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.settings.InfageStyle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends Screen {
	public DeviceEntity deviceEntity;
	public static final Logger LOGGER;
	private TextFieldWidget codeField;

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
	}

	public InfageDeviceScreen(DeviceEntity deviceEntity) {
		super(new TranslatableText(deviceEntity.getCachedState().getBlock().getTranslationKey()));
		this.deviceEntity = deviceEntity;
	}

	@Override
	public void tick() {
		this.codeField.tick();
	}

	@Override
	public void init() {
		// 代码域
		this.codeField = new TextFieldWidget(this.textRenderer, //
				(int) (this.width * InfageStyle.code[0]), //
				(int) (this.height * InfageStyle.code[1]), //
				(int) (this.width * InfageStyle.code[2]), //
				(int) (this.height * InfageStyle.code[3]), //
				new TranslatableText("structure_block.structure_name")) {
			public boolean charTyped(char chr, int modifiers) {
				return !InfageDeviceScreen.this.isValidCharacterForName(this.getText(), chr, this.getCursor()) ? false : super.charTyped(chr, modifiers);
			}
		};
		this.codeField.setMaxLength(64);
		this.codeField.setText("Code Field");
		this.children.add(this.codeField);


		this.setInitialFocus(this.codeField);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String string = this.codeField.getText();
		this.init(client, width, height);
		this.codeField.setText(string);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.codeField.render(matrices, mouseX, mouseY, delta);
	}
}
