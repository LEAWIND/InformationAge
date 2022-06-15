package net.leawind.infage.client.gui.widget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.leawind.infage.settings.InfageSettings;
import net.leawind.infage.util.KeyCode;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// 可编辑多行文本域
public class MultilineTextFieldWidget extends AbstractButtonWidget {
	private static Keyboard CLIPBOARD = MinecraftClient.getInstance().keyboard;
	private TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer; // 获取文本渲染器
	private static final String SEPERATOR = " \n\t.`'\";:|";
	private static final Identifier TEXTURE_BG = new Identifier(InfageSettings.NAMESPACE, "textures/gui/codefield_background.png");
	private boolean isEditable = true;
	private int maxLength = 16384;

	private String content = "";
	private int windowWidth = 30;
	private int windowHeight = 20;
	private int windowX = 0;
	private int windowY = 0;
	private int cursorI = 0;
	private int cursorX = 0;
	private int cursorY = 0;
	private boolean isSelecting = false;
	private int selectStart = 0;
	private int selectEnd = 0;
	private Map<KeyCombination, KeyEventHandler> keyBindings = new HashMap<>();

	public MultilineTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
		super(x, y, width, height, text);
		this.registerDefaultKeyBindings(); // 注册按键绑定
		// this.windowHeight = this.height / this.textRenderer.fontHeight; // 计算窗口中可以显示多少行
	}

	// 注册默认键盘事件
	private void registerDefaultKeyBindings() {
		this.registerKey(new KeyCombination('W', 0b0000), () -> {
			System.out.println("kvt: W");
		});
		this.registerKey(new KeyCombination(KeyCode.ESC, 0b0000), () -> {
			System.out.println("kvt: Esc");
		});
		this.registerKey(new KeyCombination(KeyCode.Letter('S'), 0b0001), () -> {
			System.out.println("kvt: 0b001 + S");
		});
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		KeyCombination kb = new KeyCombination(keyCode, modifiers);
		System.out.printf("Clicked = [%d]kb %s\n", kb.hashCode(), kb);
		if (this.keyBindings.containsKey(kb)) {
			System.out.println("Key event handler found !!");
			this.keyBindings.get(kb).exec();
		}
		return true;
	}


	public void registerKey(KeyCombination kb, KeyEventHandler h) {
		this.keyBindings.put(kb, h);
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (this.visible) {
			// TODO render MLF
			this.renderBackground(matrices, mouseX, mouseY, delta);
			this.renderTexts(matrices, mouseX, mouseY, delta);
		}
	}


	public void tick() {}

	// 设置长度上限
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	// 设置是否可以编辑
	public void setEditable(boolean b) {
		this.isEditable = b;
	}

	/**
	 * 渲染相关函数
	 */

	// 绘制背景
	private void renderBackground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.getTextureManager().bindTexture(TEXTURE_BG);
		drawTexture(matrices, this.x + 5, this.y + 5, 0, 0, this.width - 10, this.height - 10, 512, 512); // 中
		minecraftClient.getTextureManager().bindTexture(WIDGETS_LOCATION);
		drawTexture(matrices, this.x, this.y, 5, 5, 24, 23, 3, 3, 256, 256); // 左上
		drawTexture(matrices, this.x, this.y + this.height - 5, 5, 5, 24, 23 + 22 - 3, 3, 3, 256, 256); // 左下
		drawTexture(matrices, this.x + this.width - 5, this.y, 5, 5, 24 + 22 - 3, 23, 3, 3, 256, 256); // 右上
		drawTexture(matrices, this.x + this.width - 5, this.y + this.height - 5, 5, 5, 24 + 22 - 3, 23 + 22 - 3, 3, 3, 256, 256); // 右下
		drawTexture(matrices, this.x + 5, this.y, this.width - 2 * 5, 5, 24 + 3, 23, 22 - 2 * 3, 3, 256, 256); // 上
		drawTexture(matrices, this.x + 5, this.y + this.height - 5, this.width - 2 * 5, 5, 24 + 3, 23 + 22 - 3, 22 - 2 * 3, 3, 256, 256); // 下
		drawTexture(matrices, this.x, this.y + 5, 5, this.height - 2 * 5, 24, 23 + 3, 3, 22 - 2 * 3, 256, 256); // 左
		drawTexture(matrices, this.x + this.width - 5, this.y + 5, 5, this.height - 2 * 5, 24 + 22 - 3, 23 + 3, 3, 22 - 2 * 3, 256, 256); // 右
	}

	// TODO绘制文本
	private void renderTexts(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		// windowX|Y
		// 计算显示区域的字符串列表
		String[] allLines = this.content.split("\n");
		String[] vlines = Arrays.copyOfRange(allLines, this.windowY, this.windowY + this.windowHeight);
		for (int i = 0; i < vlines.length; i++) {
			String line;
			if (this.windowX < vlines[i].length()) {
				line = vlines[i].substring(this.windowX, this.windowX + Math.min(this.windowWidth, vlines[i].length()));
			} else
				line = "";
			// TODO 绘制该行
			int dx, dy;
			dx = 0;
			dy = this.textRenderer.fontHeight * i;

			// drawStringWithShadow(matrices, this.textRenderer, line, 0, this.textRenderer.fontHeight * i,
			// 0xFFFFFFFF);
		}
	}

	/**
	 * 功能相关函数
	 */

	// 读取剪切板
	public static String getClipboard() {
		// 获取剪贴板中的内容
		return CLIPBOARD.getClipboard();
	}

	// 设置剪切板
	public static void setClipboard(String str) {
		CLIPBOARD.setClipboard(str);
	}

	// 获取字符串
	public String getString() {
		return this.content;
	}

	// 设置字符串
	public void setString(String str) {
		this.content = str;
		this.setCursorCoord(this.cursorX, this.cursorY);

	}

	// 在光标处插入字符串
	public void insertString(String str) {
		this.insertString(str, this.cursorI);
	}

	// 在指定位置插入字符串
	public void insertString(String str, int index) {
		assert (index >= 0 && index < this.content.length());
		this.content = this.content.substring(0, index) + str + this.content.substring(index);
		this.setCursorIndex(index + str.length());
	}

	// 在光标处插入字符
	public void insert(char c) {
		this.insert(c, this.cursorI);
	}

	// 在指定位置插入字符
	public void insert(char c, int index) {
		this.insertString(String.valueOf(c), index);
	}

	// 获取光标索引
	public int getCursorIndex() {
		int i = 0;
		int x = 0, y = 0;
		while (i < this.content.length()) {
			char c = this.content.charAt(i);
			if (x == cursorX && y == cursorY)
				break;
			if (c == '\n') {
				y++;
				x = 0;
			} else {
				x++;
			}
			i++;
		}
		return i;
	}

	// 设置光标索引
	private void setCursorIndex(int i) {
		i = Math.max(0, Math.min(this.content.length(), i));
		this.cursorI = i;
		int[] cc = this.getIndexCoord(i);
		this.cursorX = cc[0];
		this.cursorY = cc[1];
	}

	// 设置光标位置
	public void setCursorCoord(int x, int y) {
		int ix = 0, iy = 0, ic = 0;
		for (; ic < this.content.length(); ic++) {
			char c = this.content.charAt(ic);
			if (iy == y) {
				if (c == '\n') {
					break;
				} else if (ix == x) {
					break;
				} else {
					ix++;
				}
			} else if (c == '\n') {
				iy++;
				ix = 0;
			}
		}
		this.cursorI = ic;
	}

	// 获取光标位置
	public int[] getIndexCoord(int ind) {
		int x = 0, y = 0;
		for (int i = 0; i < ind; i++) {
			char c = this.content.charAt(i);
			if (c == '\n') {
				y++;
				x = 0;
			} else
				x++;
		}
		return new int[] {x, y};
	}

	// 获取选取的字符串
	public String getSelected() {
		int i = this.selectStart < this.selectEnd ? this.selectStart : this.selectEnd;
		int j = this.selectStart < this.selectEnd ? this.selectEnd : this.selectStart;
		return this.content.substring(i, j);
	}

	// 剪切选取部分
	public void cut() {
		if (this.isSelecting) {
			this.copy();
			this.deleteSelected();
		}
	}

	// 删除选取部分
	private void deleteSelected() {
		int i = this.selectStart < this.selectEnd ? this.selectStart : this.selectEnd;
		int j = this.selectStart < this.selectEnd ? this.selectEnd : this.selectStart;
		this.setCursorIndex(i);
		this.content = this.content.substring(0, i) + this.content.substring(j);
	}

	// 复制选取部分
	public void copy() {
		if (this.isSelecting) {
			setClipboard(this.getSelected());
		}
	}

	// 粘贴
	public void paste() {
		String str = getClipboard();
		if (this.isSelecting)
			this.deleteSelected();
		this.content = this.content.substring(0, this.cursorI) + str + this.content.substring(this.cursorI);
		this.setCursorIndex(this.cursorI + str.length());
	}

	// 输入
	public void write(String str) {
		if (this.isSelecting)
			this.deleteSelected();
		this.content = this.content.substring(0, this.cursorI) + str + this.content.substring(this.cursorI);
		this.setCursorIndex(this.cursorI + str.length());
	}

	// 删除
	public void erase() {
		if (this.isSelecting)
			this.deleteSelected();
		this.deleteLeft();
	}

	// 选择全部
	public void selectAll() {
		this.selectStart = 0;
		this.selectEnd = this.content.length();
	}

	// 删除光标所在行
	public void deleteLine() {
		int i0 = this.content.lastIndexOf("\n", this.cursorI);
		if (i0 == -1)
			i0 = 0;
		int i1 = this.content.indexOf("\n", this.cursorI);
		if (i1 == -1)
			i1 = this.content.length();
		this.content = this.content.substring(0, i0) + this.content.substring(i1);
		if (this.cursorI > this.content.length())
			this.setCursorIndex(this.content.length());
	}

	// 获取光标所在行
	public String getLine() {
		int i0 = this.content.lastIndexOf("\n", this.cursorI);
		if (i0 == -1)
			i0 = 0;
		int i1 = this.content.indexOf("\n", this.cursorI);
		if (i1 == -1)
			i1 = this.content.length();
		String line = this.content.substring(i0, i1);
		return line;
	}

	// 剪切光标所在行
	public void cutLine() {
		setClipboard(this.getLine());
		this.deleteLine();
	}


	public void copyLineDown() {}

	public void copyLineUp() {}

	public void copyCellDown() {}

	public void copyCellUp() {}

	public void moveLineUp() {}

	public void moveLineDown() {}

	// 在下方插入行
	public void insertLineBelow() {
		this.cursorEnd();
		this.insert('\n');
	}

	public void insertLineAbove() {}

	public void outdentLine() {

	}

	public void indentLine() {}

	public void undo() {}

	// 光标移动到底部
	public void cursorMoveBottom() {
		this.setCursorIndex(this.content.length());
	}

	// 光标移动到底部并选择
	public void cursorMoveBottomSelect() {
		this.selectStart = this.cursorI;
		this.cursorMoveBottom();
		this.selectEnd = this.cursorI;
		if (this.selectEnd - this.selectStart > 0)
			this.isSelecting = true;
	}

	public void cursorMoveTop() {}

	public void cursorMoveSelectTop() {}

	// 光标左移
	public void cursorMoveLeft() {
		this.setCursorIndex(this.cursorI - 1);
	}

	public void cursorMoveSelectLeft() {}

	// 光标右移
	public void cursorMoveRight() {
		this.setCursorIndex(this.cursorI + 1);
	}

	public void cursorMoveSelectRight() {}


	// 移动光标到行首
	public void cursorHome() {
		this.cursorX = 0;
	}

	public void cursorHomeSelect() {}

	// 移动光标到行末
	public void cursorEnd() {
		int i = this.cursorI;
		while (this.content.charAt(i) != '\n' && i < this.content.length()) {
			i++;
		}
		this.setCursorIndex(i);
	}

	public void cursorEndSelect() {}

	public void selectWord() {}

	// 删除光标左侧字符
	public void deleteLeft() {
		if (this.cursorI <= 0)
			return;
		this.setCursorIndex(this.cursorI - 1);
		this.deleteRight();
	}


	// 删除右侧字符
	public void deleteRight() {
		int leng = this.content.length();
		int ind = this.cursorI;
		if (ind == leng - 1) {
			this.content = this.content.substring(0, ind);
		} else {
			this.content = this.content.substring(0, ind) + this.content.substring(ind + 1);
		}
	}

	public int findWordBorderLeft() {
		return 0;
	}

	public int findWordBorderRight() {
		return 0;
	}

	public void deleteWordLeft() {}

	public void deleteWordRight() {}

	// 组合键
	public static final class KeyCombination {
		int keyCode;
		int flags;

		public KeyCombination(int keyCode, int flags) {
			this.keyCode = keyCode;
			this.flags = flags;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof KeyCombination && obj.hashCode() == this.hashCode();
		}

		@Override
		public int hashCode() {
			return (this.keyCode << 5) + this.flags;
		}
	}

	// 键盘事件处理函数
	public static interface KeyEventHandler {
		public void exec();
	}



}
