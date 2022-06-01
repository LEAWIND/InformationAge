package net.leawind.infage.gui;

import java.util.List;

import com.google.common.collect.Lists;

import net.leawind.infage.blockentity.DeviceEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.TranslatableText;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends Screen {
	public static final boolean ENABLE_COMMAND_SUGGESTOR = false; // 是否启用代码提示

	// TODO Style 设置
	public static final class Style {
		public static final double tilePos[] = { 0.10, 0.00, 0.50, 0.03 }; // 标题文本位置
		public static final double codeFieldPos[] = { 0.10, 0.03, 0.50, 0.97 }; // 代码文本域位置
		public static final double outputsFieldPos[] = { 0.60, 0.40, 0.40, 0.60 }; // 输出文本域位置
		public static double doneButtonPos[] = { 0.80, 0.00, 0.20, 0.05 }; // 完成按钮位置
		public static double powerButtonPos[] = { 0.60, 0.00, 0.20, 0.05 }; // 电源按钮位置

		public static final double eventButtonsShape[] = { 0.10, 0.06 }; // 事件按钮宽高
		public static final double portsButtonsShape[] = { 0.85, 0.04 }; // 端口按钮宽高
		public static final double portsButtonsPos[] = { 0.1, 0.2 }; // 端口按钮起始位置
		public static final int portsCountPerLine = 4; // 每行多少个端口按钮
	};

	public BlockEntity deviceEntity; // 对应的设备方块

	// 针对每一个事件脚本，都有历史记录栈。每输入一个非字母，就添加一次记录
	// public List<List<String>> ScriptHistory = Lists.newArrayList();
	// 当前在历史记录中的位置
	// public List<int> ScriptHistoryPos = Lists.newArrayList();

	public TextFieldWidget codeField;// 输入代码的文本域
	public TextFieldWidget outputsField;// 输出的文本域
	public ButtonWidget doneButton; // 完成按钮，按esc也是完成
	public ButtonWidget powerButton; // 开关机按钮
	public List<ButtonWidget> portsButtons = Lists.newArrayList(); // 端口按钮(连接、断开)
	public List<ButtonWidget> eventButtons = Lists.newArrayList(); // 针对每个事件都有一个脚本
	public CommandSuggestor commandSuggestor;

	public InfageDeviceScreen(DeviceEntity deviceEntity) {
		super(NarratorManager.EMPTY);
		this.deviceEntity = deviceEntity;
	}

	// TODO 在初始化方法中绘制界面
	@Override
	public void init() {
		super.init(); // 这个方法是空的，用不用无所谓
		// 代码文本域
		{
			this.codeField = new TextFieldWidget(
					this.textRenderer,
					(int) (Style.codeFieldPos[0] * this.width),
					(int) (Style.codeFieldPos[1] * this.height),
					(int) (Style.codeFieldPos[2] * this.width),
					(int) (Style.codeFieldPos[3] * this.height),
					new TranslatableText("advMode.command"));
			this.codeField.setMaxLength(65536);
			this.codeField.setChangedListener(this::onCodeChanged);
			this.codeField.setEditable(true);
			this.setInitialFocus(this.codeField);
			this.codeField.setTextFieldFocused(true);

			if (ENABLE_COMMAND_SUGGESTOR) {
				// TODO 这个命令提示需要自定义（貌似工作量比较大）
				this.commandSuggestor = new CommandSuggestor(this.client, this, this.codeField,
						this.textRenderer,
						true, true, 0, 7, false, Integer.MIN_VALUE);
				this.commandSuggestor.setWindowActive(true);
				this.commandSuggestor.refresh();
			}
		}
		{
			this.outputsField = new TextFieldWidget(
					this.textRenderer,
					(int) (Style.outputsFieldPos[0] * this.width),
					(int) (Style.outputsFieldPos[1] * this.height),
					(int) (Style.outputsFieldPos[2] * this.width),
					(int) (Style.outputsFieldPos[3] * this.height),
					new TranslatableText("advMode.command"));
			this.outputsField.setMaxLength(65536);
			this.outputsField.setEditable(false);
			this.outputsField.setText("Script Outputs");
		}

		this.doneButton = (ButtonWidget) this.addButton(
				new ButtonWidget(
						(int) (Style.doneButtonPos[0] * this.width),
						(int) (Style.doneButtonPos[1] * this.height),
						(int) (Style.doneButtonPos[2] * this.width),
						(int) (Style.doneButtonPos[3] * this.height),
						ScreenTexts.CANCEL, (buttonWidget) -> {
							this.onClose(); // 触发关闭事件
						}));
		this.powerButton = (ButtonWidget) this.addButton(
				new ButtonWidget(
						(int) (Style.powerButtonPos[0] * this.width),
						(int) (Style.powerButtonPos[1] * this.height),
						(int) (Style.powerButtonPos[2] * this.width),
						(int) (Style.powerButtonPos[3] * this.height),
						ScreenTexts.CANCEL, (buttonWidget) -> {
							this.onTogglePower(buttonWidget);
						}));
		// TODO 遍历端口，添加端口按钮[port:连接|断开]
		// TODO 遍历事件，添加事件按钮[onPortConnect]

	}

	// 键盘事件
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO 应该是在这定义快捷键
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	// 是否应该在按 ESC 时关闭
	public boolean shouldCloseOnEsc() {
		return true;
	}

	// 事件：代码文本域内容发送变化s
	public void onCodeChanged(String string) {

	}

	// TODO 自定义事件：切换设备开关
	public void onTogglePower(ButtonWidget buttonWidget) {
		// 如果当前设备处于关闭状态，则打开，否则关闭
		if (((DeviceEntity) this.deviceEntity).isRunning) {

		} else {

		}
	}

	// 永远返回 true 表示这个界面占满整个屏幕，无论光标在哪都在屏幕上
	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return true;
	}

	// 重置尺寸
	@Override
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);
		this.doneButton.active = true;
		this.powerButton.active = true;

		// this.portsButtons.active = true;
		// this.eventButtons.active = true;
	}

}
