package net.leawind.infage.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

// 可编辑多行文本域
public class MultilineTextFieldWidget extends TextFieldWidget {
	public MultilineTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
		super(textRenderer, x, y, width, height, text);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		System.out.println("Multiline Text field widget: key pressed.");
		System.out.printf("keyCode = %d, modifiers = %d %d %d\n", keyCode, modifiers & 0b100, modifiers & 0b010, modifiers & 0b001);
		super.keyPressed(keyCode, scanCode, modifiers);
		return true;
	}
}
