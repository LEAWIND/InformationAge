package net.leawind.infage.client.gui.screen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.client.gui.widget.MultilineTextFieldWidget;
import net.leawind.infage.network.UpdateDeviceC2SPacket;
import net.leawind.infage.settings.InfageStyle;
import net.leawind.infage.settings.InfageTexts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

// Screen extends DrawableHelper
public class InfageDeviceScreen extends Screen {
	public BlockPos blockPos;

	public static final Logger LOGGER;

	private boolean isRunning = false;
	private boolean hasItemSlots = false;
	private int portsCount = 0;

	private TextFieldWidget codeField; // 代码域
	private TextFieldWidget outputsField; // 输出域

	private ButtonWidget doneButton; // 完成按钮
	private ButtonWidget powerButton; // 电源按钮
	private ButtonWidget[] portsButtons; // 接口按钮们

	private byte[] portsStatus; // 接口状态

	static {
		LOGGER = LogManager.getLogger("InfageDeviceScreen");
	}

	public InfageDeviceScreen(BlockPos pos, Text text) {
		// deviceEntity.getCachedState().getBlock().getTranslationKey())
		super(text);
		this.blockPos = pos;
		this.portsButtons = new ButtonWidget[this.portsCount]; // 初始化接口按钮数组
		this.act(DeviceEntity.Action.GET_DATA); // 从服务器获取数据
	}

	@Override
	public void tick() {
		this.codeField.tick();
	}

	@Override
	public void init() {
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

		this.setInitialFocus(this.codeField);
	}


	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String string0 = this.codeField.getText();
		String string1 = this.outputsField.getText();
		this.init(client, width, height);
		this.codeField.setText(string0);
		this.outputsField.setText(string1);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.codeField.render(matrices, mouseX, mouseY, delta);
		this.outputsField.render(matrices, mouseX, mouseY, delta);
		super.render(matrices, mouseX, mouseY, delta);
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

	// 结束
	public void done() {
		this.act(DeviceEntity.Action.UPDATE_DATA);
		this.client.openScreen((Screen) null);
	}

	// TODO 按下电源键
	public void onTogglePower() {
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

}
