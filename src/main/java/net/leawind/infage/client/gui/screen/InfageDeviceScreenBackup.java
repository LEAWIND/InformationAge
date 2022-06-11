package net.leawind.infage.client.gui.screen;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Lists;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.client.gui.widget.MultilineTextFieldWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Screen extends DrawableHelper
public class InfageDeviceScreenBackup extends HandledScreen<ScreenHandler> {
	private static final Logger LOGGER;
	public static boolean ENABLE_COMMAND_SUGGESTOR = false; // 是否启用代码提示

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
	}

	// Style 设置
	public static final class Style {
		public static final double tilePos[] = {0.10, 0.00, 0.50, 0.03}; // 标题文本 位置
		public static final double codeFieldPos[] = {0.10, 0.03, 0.50, 0.97}; // 代码文本域 位置
		public static final double outputsFieldPos[] = {0.60, 0.40, 0.40, 0.60}; // 输出文本域 位置
		public static double doneButtonPos[] = {0.80, 0.00, 0.20, 0.05}; // 完成按钮 位置
		public static double powerButtonPos[] = {0.60, 0.00, 0.20, 0.05}; // 电源按钮 位置

		public static final double eventButtonPos[] = {0.00, 0.00}; // 事件按钮 宽高
		public static final double eventButtonMargin = 1.2;

		public static final double portsButtonShape[] = {0.085, 0.04}; // 端口按钮 宽高
		public static final double portsButtonPos[] = {0.61, 0.06}; // 端口按钮起始 位置
		public static final double portButtonMargin = 1.2;
		public static final int portsCountPerLine = 4; // 每行多少个端口按钮
	};

	public World world; // 所在的世界
	public BlockPos devicePos; // 设备坐标
	public DeviceEntity deviceEntity; // 对应的设备方块实体

	public MultilineTextFieldWidget codeField;// 输入代码的文本域
	public MultilineTextFieldWidget outputsField;// 输出的文本域
	public ButtonWidget doneButton; // 完成按钮，按esc也是完成
	public ButtonWidget powerButton; // 开关机按钮
	public List<ButtonWidget> portsButtons = Lists.newArrayList(); // 端口按钮(连接、断开)
	public CommandSuggestor commandSuggestor;

	public ButtonWidget testButton; //

	public InfageDeviceScreenBackup(ScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	// 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init();
		System.out.println("Infage Device Screen: init");
		this.deviceEntity = (DeviceEntity) this.world.getBlockEntity(this.devicePos);
		if (this.deviceEntity.portsCount == 0) {
			System.out.println("God damn");
		}
		titleX = (int) (width * 0.5);
		titleY = (int) (height * 0.0);
		// 列 1
		// 列 2
		{
			// 代码文本域
			{
				this.codeField = new MultilineTextFieldWidget(this.textRenderer, (int) (Style.codeFieldPos[0] * this.width), (int) (Style.codeFieldPos[1] * this.height), (int) (Style.codeFieldPos[2] * this.width), (int) (Style.codeFieldPos[3] * this.height), new TranslatableText("advMode.command"));

				this.codeField.setMaxLength(65536);
				this.codeField.setChangedListener(this::onCodeChanged);
				this.codeField.setEditable(true);
				this.setInitialFocus(this.codeField);
				this.codeField.setTextFieldFocused(true);

				if (ENABLE_COMMAND_SUGGESTOR) {
					this.commandSuggestor = new CommandSuggestor(this.client, this, this.codeField, this.textRenderer, true, true, 0, 7, false, Integer.MIN_VALUE);
					this.commandSuggestor.setWindowActive(true);
					this.commandSuggestor.refresh();
				}
			}
		}
		// 列3
		{
			int y = 0;
			// 完成按钮
			this.doneButton = (ButtonWidget) this.addButton(new ButtonWidget((int) (Style.doneButtonPos[0] * this.width), (int) (Style.doneButtonPos[1] * this.height), (int) (Style.doneButtonPos[2] * this.width), (int) (Style.doneButtonPos[3] * this.height), ScreenTexts.DONE, (buttonWidget) -> {
				this.onClose();
			}));

			// 电源按钮
			this.powerButton =
					(ButtonWidget) this.addButton(new ButtonWidget((int) (Style.powerButtonPos[0] * this.width), (int) (Style.powerButtonPos[1] * this.height), (int) (Style.powerButtonPos[2] * this.width), (int) (Style.powerButtonPos[3] * this.height), InfageScreenTexts.BOOT, this::onTogglePower));
			y += (int) (Style.powerButtonPos[3] * this.height + (Style.portButtonMargin - 1) * Style.powerButtonPos[3]);

			// 遍历端口，添加端口按钮 [port:连接|断开]
			for (int i = 0, n = 0; i < this.deviceEntity.portsCount; i += Style.portsCountPerLine) {
				for (int j = 0; j < Style.portsCountPerLine; j++) {
					ButtonWidget portButton =
							new ButtonWidget((int) ((Style.portsButtonPos[0] + Style.portsButtonShape[0] * j) * this.width), y, (int) (Style.portsButtonShape[0] * this.width), (int) (Style.portsButtonShape[1] * this.height), new LiteralText("Port" + n + ":" + "OK"), this::onPressPortButton);
					this.portsButtons.add((ButtonWidget) this.addButton(portButton));
					n++;
				}
				y += (int) (Style.portsButtonShape[1] * this.height * Style.portButtonMargin);
			}

			// 输出文本域
			{
				this.outputsField = new MultilineTextFieldWidget(this.textRenderer, (int) (Style.outputsFieldPos[0] * this.width),
						// (int) (Style.outputsFieldPos[1] * this.height),
						y, (int) (Style.outputsFieldPos[2] * this.width), this.height - y, new TranslatableText("advMode.command"));
				this.outputsField.setMaxLength(65536);
				this.outputsField.setEditable(false);
				this.outputsField.setText("Script Outputs");
			}
		}
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

	}

	// 键盘事件
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO 应该是在这定义快捷键
		// Ctrl + Z
		// Ctrl + Shift + Z
		// Ctrl + Enter
		// ctrl + Backspace
		// ctrl + /
		// ctrl + X
		// ctrl + V
		// ctrl + C
		// ctrl + A
		// ctrl + F
		// Shift + Alt + F
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	// 是否应该在按 ESC 时关闭
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public void tick() {
		// this.codeField.tick();
		// this.outputsField.tick();
	}

	@Override
	public void removed() {
		this.client.keyboard.setRepeatEvents(false);
	}

	// 重置尺寸
	@Override
	public void resize(MinecraftClient client, int width, int height) {
		LOGGER.info("Resize");
		super.resize(client, width, height);
		this.init();
		this.doneButton.active = true;
		this.powerButton.active = true;
		// for (int i = 0; i < this.portsButtons.size(); i++) {
		// this.portsButtons.get(i).active = true;
		// }
	}

	// 渲染界面
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);

		if (this.codeField != null)
			this.codeField.render(matrices, mouseX, mouseY, delta);
		if (this.outputsField != null)
			this.outputsField.render(matrices, mouseX, mouseY, delta);

		super.render(matrices, mouseX, mouseY, delta);
		if (ENABLE_COMMAND_SUGGESTOR && this.commandSuggestor != null)
			this.commandSuggestor.render(matrices, mouseX, mouseY);
	}

	// 事件：代码文本域内容发生变化s
	public void onCodeChanged(String string) {
		LOGGER.info("Event: code changed");
	}

	// 切换设备开关
	public void onTogglePower(ButtonWidget buttonWidget) {
		// 如果当前设备处于关闭状态，则打开，否则关闭
		if (((DeviceEntity) this.deviceEntity).isRunning) {
			this.deviceEntity.device_shutdown();
		} else {
			this.deviceEntity.device_shutdown();
		}
	}

	public void onPressPortButton(ButtonWidget buttonWidget) {
		LOGGER.info("Event: press port button");

	}

	public void onPressEventButton(ButtonWidget buttonWidget) {
		LOGGER.info("Event: press event button");
	}


}
