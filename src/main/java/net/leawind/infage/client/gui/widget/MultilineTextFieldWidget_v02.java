package net.leawind.infage.client.gui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.leawind.infage.settings.InfageSettings;
import net.leawind.infage.util.KeyCode;
import net.leawind.infage.util.Modifier;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// 可编辑多行文本域 v02
// TODO 聚焦状态
public class MultilineTextFieldWidget_v02 extends AbstractButtonWidget {
	private static Keyboard CLIPBOARD = MinecraftClient.getInstance().keyboard;
	private final TextRenderer textRenderer; // 文本渲染器
	private static final String SEPERATOR_PATTERN = "[ \\t\\n.]";
	private static final Identifier TEXTURE_BG = new Identifier(InfageSettings.NAMESPACE, "textures/gui/codefield_background.png");
	private boolean editable = true;
	private int maxLength = 16384;

	private int charWidth = 6; // * fontHeight
	private int lineHeight = 10; // * fontHeight
	private int tickCounter; // tick 计数器
	private ArrayList<String> lines = new ArrayList<>(); // 行们

	private int windowWidth = 30; // 每行显示多少字符
	private int windowHeight = 20; // 每页显示多少列
	private int windowX = 0; // 窗口左上角坐标
	private int windowY = 0;
	private int lineCountStrLen = 2; // 行号宽度，动态变化

	private int cursorX = 0; // 光标坐标
	private int cursorY = 0;
	private boolean isSelecting = false; // 是否正在选择

	private int[] selectPos = {0, 0}; // 选择起始点坐标
	private int[] selectEnd = {0, 0};


	private Map<KeyCombination, KeyEventHandler> keyBindings = new HashMap<>();

	public MultilineTextFieldWidget_v02(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
		super(x, y, width, height, text);
		this.registerDefaultKeyBindings(); // 注册按键绑定
		this.textRenderer = textRenderer;
		this.charWidth = (int) (textRenderer.fontHeight * 0.7); // 字符宽度
		this.lineHeight = (int) (textRenderer.fontHeight * 1.1); // 行高
		this.windowWidth = (int) (this.width / this.charWidth) - 1; // 同时显示的列数上限
		this.windowHeight = (int) (this.height / this.lineHeight) - 1; // 计算同时显示的行数上限
		this.lines.add("");
	}

	// 注册默认键盘事件
	private void registerDefaultKeyBindings() {
		this.registerKey(new KeyCombination(KeyCode.RIGHT, 0b000), (keyCode, scanCode, flag) -> {
			this.cursorMoveRight();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.LEFT, 0b000), (keyCode, scanCode, flag) -> {
			this.cursorMoveLeft();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.UP, 0b000), (keyCode, scanCode, flag) -> {
			this.cursorMoveUp();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.DOWN, 0), (keyCode, scanCode, flag) -> {
			this.cursorMoveDown();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.RIGHT, Modifier.SHIFT), (keyCode, scanCode, flag) -> {
			this.scrollBy(2, 0);
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.LEFT, Modifier.SHIFT), (keyCode, scanCode, flag) -> {
			this.scrollBy(-2, 0);
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.UP, Modifier.SHIFT), (keyCode, scanCode, flag) -> {
			this.scrollBy(0, -2);
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.DOWN, Modifier.SHIFT), (keyCode, scanCode, flag) -> {
			this.scrollBy(0, 2);
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.HOME, 0b000), (keyCode, scanCode, flag) -> {
			this.cursorHome();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.END, 0b000), (keyCode, scanCode, flag) -> {
			this.cursorEnd();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.BACKSPACE, 0b000), (keyCode, scanCode, flag) -> {
			this.deleteLeft();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.DELETE, 0b000), (keyCode, scanCode, flag) -> {
			this.deleteRight();
			this.makeCursorVisible();
			return true;
		});
		this.registerKey(new KeyCombination(KeyCode.ENTER, 0b000), (keyCode, scanCode, flag) -> {
			this.insertString("\n", this.cursorX, cursorY);
			this.makeCursorVisible();
			return true;
		});

		/*
		 * Need to Test
		 */

		this.registerKey(null, (keyCode, scanCode, flag) -> {
			System.out.printf("UK KEVT: kc=%d, sc=%d, f= %d, %d, %d, %d\n", keyCode, scanCode, flag & 0b1000, flag & 0b0100, flag & 0b0010, flag & 0b0001);
			return false;
		});
	}


	// 原生键盘事件
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int flag) {
		if (!this.isEditable())
			return false;
		KeyCombination kb = new KeyCombination(keyCode, flag);
		if (this.keyBindings.containsKey(kb)) {
			return this.keyBindings.get(kb).exec(keyCode, scanCode, flag);
		} else if (this.keyBindings.containsKey(null)) {
			return this.keyBindings.get(null).exec(keyCode, scanCode, flag);
		} else {
			return true;
		}
	}

	// 输入字符事件
	@Override
	public boolean charTyped(char ch, int flag) {
		if (!this.editable)
			return false;
		// System.out.printf("char typed: [%c], f= %d, %d, %d, %d\n", ch, flag & 0b1000, flag & 0b0100, flag
		// & 0b0010, flag & 0b0001);
		if (this.isSelecting)
			this.deleteSelected();
		String line = this.getCursorLine();
		line = line.substring(0, this.cursorX) + Character.toString(ch) + line.substring(this.cursorX);
		this.lines.set(this.cursorY, line);
		this.cursorMoveRight();
		this.makeCursorVisible();
		return false;
	}

	// 注册按键绑定
	public void registerKey(KeyCombination kb, KeyEventHandler h) {
		this.keyBindings.put(kb, h);
	}

	// 渲染本组件
	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (this.visible) {
			this.renderBackground(matrices, mouseX, mouseY, delta);
			this.renderTexts(matrices, mouseX, mouseY, delta);
		}
	}

	//
	public void tick() {
		this.tickCounter++;
	}

	// 设置长度上限
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	// 获取最大长度
	public int getMaxLength() {
		return this.maxLength;
	}

	// 是否可以编辑
	public boolean isEditable() {
		return this.editable;
	}

	// 设置是否可以编辑
	public void setEditable(boolean b) {
		this.editable = b;
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

	// 计算行号文本
	private String getLineIndexString(int lineIndex, int width) {
		return String.format(Locale.CHINA, "%" + width + "d", lineIndex) + "|";
	}

	// 绘制等宽文本
	private void drawMonoText(MatrixStack matrices, int x, int y, String text, int charWidth, int color) {
		for (int i = 0; i < text.length(); i++)
			this.textRenderer.draw(matrices, text.substring(i, i + 1), x + (float) charWidth * i, y, color);
	}

	// TODO 绘制文本
	private void renderTexts(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		// 计算行号字符串最大宽度
		int lc10 = (int) Math.log10(this.windowY + this.windowHeight) + 1;
		// TODO FATAL 计算显示区域的字符串列表 java.lang.IndexOutOfBoundsException: fromIndex = -2
		List<String> visiblelines = this.lines.subList(this.windowY, Math.min(this.windowY + this.windowHeight, this.lines.size()));
		int i = 0;
		for (String line : visiblelines) {
			if (this.windowX > line.length())
				continue;
			line = line.substring(this.windowX); // 裁剪
			int lineCount = this.windowY + i; // 行号
			String lineCountString = this.getLineIndexString(lineCount, lc10); // 计算行号文本
			this.lineCountStrLen = lineCountString.length(); // 行号文本长度
			line = lineCountString + line; // 在行文本前加上行号
			if (line.length() > this.windowWidth) // 裁剪到窗口宽度
				line = line.substring(0, this.windowWidth);
			int dx = this.x + 5;
			int dy = (int) (this.y + 5 + i * this.lineHeight);
			this.drawMonoText(matrices, dx, dy, line, this.charWidth, 0xFFFFFFFF);
			i++;
		}
		// 绘制光标
		if (this.editable && (this.tickCounter % 8 < 4)) { // 可编辑
			if ((this.cursorY >= this.windowY) && (this.cursorY <= this.windowY + this.windowHeight)) { // 光标在可见区域内 Y
				if (this.cursorX >= this.windowX && this.cursorX <= this.windowX + this.windowWidth) { // 光标在可见区域内 X
					int cx = this.x + 5 + this.charWidth * (this.cursorX - this.windowX + this.lineCountStrLen);
					int cy = this.y + 5 + this.lineHeight * (this.cursorY - this.windowY);
					int dy = cy + this.lineHeight;
					drawVerticalLine(matrices, cx, cy - 2, dy, 0xFFFFFF00);
					drawVerticalLine(matrices, cx + 1, cy - 2, dy, 0xFFFFFF00);
				}
			}
		}
	}

	/**
	 * 功能相关函数
	 */

	// TODO 读取剪切板
	public static String getClipboard() {
		// 获取剪贴板中的内容
		return CLIPBOARD.getClipboard();
	}

	// TODO 设置剪切板
	public static void setClipboard(String str) {
		CLIPBOARD.setClipboard(str);
	}

	// 获取字符串
	public String getString() {
		if (this.lines.size() == 0)
			this.lines.add("");
		if (this.lines.size() == 0)
			return "";
		return String.join("\n", this.lines);
	}

	// 设置字符串
	public void setString(String str) {
		if (this.lines.size() == 0)
			this.lines.add("");
		if (str.length() > this.maxLength)
			str = str.substring(0, this.maxLength);
		this.lines.clear();
		String[] linesArr = str.split("\n", str.length() + 2);
		for (String line : linesArr)
			this.lines.add(line);
		this.setCursorCoord(this.cursorX, this.cursorY);
	}

	// TODO 滚动到指定位置
	public void scrollTo(int x, int y) throws IndexOutOfBoundsException {
		if (this.lines.size() == 0)
			this.lines.add("");
		if (y > this.lines.size())
			throw new IndexOutOfBoundsException("Trying to scroll to line " + y + " > " + this.lines.size());
		this.windowY = y;
		int maxX = 0; // 求窗口中的最大 x
		for (String line : lines)
			maxX = Math.max(maxX, line.length());
		this.windowX = Math.min(maxX - 1, x);
	}

	// TODO 滚动
	public void scrollBy(int x, int y) {
		this.scrollTo(this.windowX + x, Math.min(this.windowY + y, this.lines.size()));
	}

	// TODO 移动窗口到能看见光标的位置
	public void makeCursorVisible() {
		int dx = 0, dy = 0;
		int cx = this.cursorX, cy = this.cursorY;
		if (cx < this.windowX)
			dx = cx - this.windowX;
		if (cx > this.windowX + this.windowWidth)
			dx = cx - (this.windowX + this.windowWidth);
		if (cy < this.windowY)
			dx = cy - this.windowY;
		if (cy > this.windowY + this.windowHeight)
			dx = cy - (this.windowY + this.windowHeight);
		// System.out.printf("cx,cy=(%d, %d)\n", this.cursorX, this.cursorY);
		System.out.printf("dx,dy=(%d, %d)\n", dx, dy);
		if ((dx | dy) != 0) {
			this.scrollBy(dx, dy);
		}
	}

	// 设置光标位置 (列， 行)
	private void setCursorCoord(int x, int y) {
		if (this.lines.size() == 0)
			this.lines.add("");
		if (y >= this.lines.size())
			y = this.lines.size() - 1;
		if (x > this.lines.get(y).length())
			x = this.lines.get(y).length();
		this.cursorX = x;
		this.cursorY = y;
	}

	// 设置光标索引
	private void setCursorIndex(int i) {
		if (this.lines.size() == 0)
			this.lines.add("");
		int[] c = this.calcCoordOfIndex(i);
		this.setCursorCoord(c[0], c[1]);
	}

	// 由索引计算光标位置
	private int[] calcCoordOfIndex(int ind) {
		if (this.lines.size() == 0)
			this.lines.add("");
		int i = 0, y = 0;
		for (String line : this.lines) {
			// 计算行长度
			int leng = line.length();
			if (i <= ind && ind <= i + leng)
				return new int[] {ind - i, y};
			i += leng + 1;
			y++;
		}
		return new int[] {0, 0};
	}

	// 由坐标计算光标索引
	private int calcIndexOfCoord(int x, int y) {
		if (this.lines.size() == 0)
			this.lines.add("");
		int ny = 0, ind = 0;
		for (String line : this.lines) {
			if (ny == y)
				return ind + x;
			ny++;
			ind += line.length() + 1;
		}
		return 0;
	}

	// TODO 获取选取区域的头尾坐标
	private int[][] getSelectCoords() {
		int[] i0;
		int[] i1;
		if (this.selectPos[1] > this.selectEnd[1]) { // 按行确定先后
			i0 = this.selectEnd;
			i1 = this.selectPos;
		} else if (this.selectPos[0] > this.selectEnd[0]) { // 按列确定先后
			i0 = this.selectEnd;
			i1 = this.selectPos;
		} else {
			i0 = this.selectPos;
			i1 = this.selectEnd;
		}
		return new int[][] {i0, i1};
	}

	// TODO 获取选取的字符串
	private String getSelected() {
		int[][] sc = this.getSelectCoords();
		int[] i0 = sc[0];
		int[] i1 = sc[1];

		if (i0[1] == i1[1]) { // 如果在同一行
			return this.lines.get(i0[1]).substring(i0[0], i1[0]);
		} else {
			ArrayList<String> slines = (ArrayList<String>) this.lines.subList(i0[1], i1[1] + 1); // 包括 i0 -> i1 所在行
			String strFirst = slines.remove(0); // i0 所在行
			String strLast = slines.remove(slines.size() - 1); // i1 所在行

			String str = strFirst.substring(i0[0]) + "\n";
			for (String li : slines)
				str += li + "\n";

			str += strLast.substring(0, i1[0]);
			return str;
		}
	}

	// TODO 剪切选取部分
	private void cut() {
		if (this.isSelecting) {
			this.copy();
			this.deleteSelected();
		}
	}

	// TODO 删除选取部分
	private void deleteSelected() {
		int[][] sc = this.getSelectCoords();
		int[] i0 = sc[0];
		int[] i1 = sc[1];

		if (i0[1] == i1[1]) {// 在同一行
			String line = this.lines.get(i0[1]);
			line = line.substring(0, i0[0]) + line.substring(i1[0]);
			this.lines.set(i0[1], line);
		} else {
			String line;
			// i0 所在行
			line = this.lines.get(i0[1]);
			this.lines.set(i0[1], line.substring(0, i0[0]));
			// i1 所在行
			line = this.lines.get(i1[1]);
			this.lines.set(i1[1], line.substring(i1[0]));
			// 删除中间行
			int i = i1[1] - i0[1];
			while (--i > 0)
				this.lines.remove(i0[1] + 1);
		}
		// 设置光标
		this.setCursorCoord(i0[0], i0[1]);
	}

	// TODO复制选取部分
	private void copy() {
		if (this.isSelecting) {
			setClipboard(this.getSelected());
		}
	}

	// TODO 粘贴
	private void paste() {
		String str = getClipboard();
		if (this.isSelecting) {// 删除已选
			this.deleteSelected();
			this.isSelecting = false;
		}
		this.insertString(str, this.cursorX, this.cursorY);
	}

	// 插入字符串
	private void insertString(String str, int x, int y) {
		if (this.lines.size() == 0)
			this.lines.add("");
		int ind = this.calcIndexOfCoord(x, y); // 计算插入点的索引
		String s = this.getString();
		s = s.substring(0, ind) + str + s.substring(ind);
		this.setString(s);
		this.setCursorIndex(ind + str.length());
	}

	// 输入
	private void write(String str) {
		if (this.isSelecting)
			this.deleteSelected();
		this.insertString(str, this.cursorX, this.cursorY);
	}

	// 删除
	private void erase() {
		if (this.isSelecting)
			this.deleteSelected();
		this.deleteLeft();
	}

	// 选择全部
	private void selectAll() {
		this.selectPos[0] = 0;
		this.selectPos[1] = 0;
		int x = this.lines.get(this.lines.size() - 1).length();
		int y = this.lines.size();
		this.selectEnd[0] = x;
		this.selectEnd[1] = y;
		this.setCursorCoord(x, y);
	}

	// 删除光标所在行
	private void deleteCursorLine() {
		this.lines.remove(this.cursorY);
		this.setCursorCoord(this.cursorX, this.cursorY);
	}

	// 获取光标所在行
	private String getCursorLine() {
		return this.lines.get(this.cursorY);
	}

	// 剪切光标所在行
	private void cutLine() {
		setClipboard(this.getCursorLine());
		this.deleteCursorLine();
	}

	// TODO将本行向下复制
	private void copyLineDown() {}

	// TODO将本行向下复制
	private void copyLineUp() {}

	// TODO将选区所在行向下复制
	private void copyCellDown() {}

	// TODO将选区所在行向上复制
	private void copyCellUp() {}

	// TODO 将行上移
	private void moveLineUp() {}

	// TODO 将行下移
	private void moveLineDown() {}

	// TODO 在下方插入行
	private void insertLineBelow() {
		this.lines.add(this.cursorY + 1, "");
		this.setCursorCoord(this.cursorX, this.cursorY + 1);
	}

	// TODO在上方插入行
	private void insertLineAbove() {}

	// TODO反缩进
	private void outdentLine() {

	}

	// TODO缩进
	private void indentLine() {}

	// 光标上移
	private void cursorMoveUp() {
		if (this.cursorY > 0)
			this.setCursorCoord(this.cursorX, this.cursorY - 1);
	}

	// 光标下移
	private void cursorMoveDown() {
		if (this.cursorY < this.lines.size() - 1)
			this.setCursorCoord(this.cursorX, this.cursorY + 1);
	}

	// TODO 光标移动到底部
	public void cursorMoveBottom() {}

	// 光标移动到底部并选择
	private void cursorMoveBottomSelect() {}

	// TODO 光标移动到顶部
	public void cursorMoveTop() {}

	// 光标移动到顶部并选择
	private void cursorMoveSelectTop() {}

	// 光标左移
	private void cursorMoveLeft() {
		if (this.cursorX == 0) {
			if (this.cursorY != 0)
				this.setCursorCoord(this.lines.get(this.cursorY - 1).length(), this.cursorY - 1);
		} else {
			this.setCursorCoord(this.cursorX - 1, this.cursorY);
		}
	}

	private void cursorMoveSelectLeft() {}

	// 光标右移
	private void cursorMoveRight() {
		if (this.cursorX == this.lines.get(this.cursorY).length()) { // 光标位于行末
			if (this.cursorY < this.lines.size() - 1) // 光标不在列尾
				this.setCursorCoord(0, this.cursorY + 1);
		} else {
			this.setCursorCoord(this.cursorX + 1, this.cursorY);
		}
	}

	private void cursorMoveSelectRight() {}


	// 移动光标到行首
	private void cursorHome() {
		this.setCursorCoord(0, this.cursorY);
	}

	private void cursorHomeSelect() {}

	// 移动光标到行末
	private void cursorEnd() {
		this.setCursorCoord(this.lines.get(this.cursorY).length(), this.cursorY);
	}

	private void cursorEndSelect() {}

	// 选择当前单词
	private void selectWord() {}

	// 删除光标左侧字符
	private void deleteLeft() {
		if (this.cursorX + this.cursorY == 0)
			return;
		this.cursorMoveLeft();
		this.deleteRight();
	}


	// 删除右侧字符
	private void deleteRight() {
		// 判断光标是不是在这一行的末尾
		if (this.cursorX == this.getCursorLine().length()) {
			// 判断光标是不是在最后一行
			if (this.cursorY < this.lines.size() - 1) {
				String nextLine = this.lines.get(this.cursorY + 1);
				// 将下一行拼到这一行后面
				this.lines.set(this.cursorY, this.getCursorLine() + nextLine);
				this.lines.remove(this.cursorY + 1);
			}
		} else {
			String line = this.getCursorLine();
			line = line.substring(0, this.cursorX) + line.substring(this.cursorX + 1);
			this.lines.set(this.cursorY, line);
		}
	}

	// TODO 向左寻找单词边界
	private int findWordBorderLeft() {
		return 0;
	}

	// TODO向右寻找单词边界
	private int findWordBorderRight() {
		return 0;
	}

	// TODO删除左边单词
	private void deleteWordLeft() {}

	// TODO删除右边单词
	private void deleteWordRight() {}

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
		public boolean exec(int keyCode, int scanCode, int flags);
	}



}
