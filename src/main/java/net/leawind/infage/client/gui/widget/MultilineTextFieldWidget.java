package net.leawind.infage.client.gui.widget;

import java.awt.datatransfer.Clipboard;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import net.leawind.infage.util.KeyCode;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;

// 可编辑多行文本域
public class MultilineTextFieldWidget extends AbstractButtonWidget {
	public static Clipboard SYS_CLIPBOARD = null;

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
	public Map<KeyCombination, KeyEventHandler> keyBindings = new HashMap<>();


	public MultilineTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
		super(x, y, width, height, text);
		this.registerDefaultKeyBindings();
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
		System.out.printf("keyCode = %d, modifiers = %d %d %d\n", keyCode, modifiers & 0b100, modifiers & 0b010, modifiers & 0b001);

		for (KeyCombination skb : this.keyBindings.keySet()) {
			System.out.printf("[%d]kb %s\n", skb.hashCode(), skb);
		}

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
	public void render(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		// TODO
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


	/**
	 * 功能相关函数
	 */

	// 读取剪切板
	public static String getClipboard() {
		// 获取剪贴板中的内容
		// Transferable trans = SYS_CLIPBOARD.getContents(null);
		// if (trans != null) {
		// if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		// try {
		// return (String) trans.getTransferData(DataFlavor.stringFlavor);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		return "";
	}

	// 设置剪切板
	public static void setClipboard(String str) {
		// Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// Transferable trans = new StringSelection(str);
		// clipboard.setContents(trans, null);
	}

	// 获取字符串
	public String getString() {
		return this.content;
	}

	// 设置字符串
	public void setString(String str) {
		this.content = str;
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
		return this.isSelecting ? this.content.substring(this.selectStart, this.selectEnd) : null;
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
		this.content = this.content.substring(0, this.selectStart) + this.content.substring(this.selectEnd);
	}

	// 复制选取部分
	public void copy() {
		if (this.isSelecting) {
			setClipboard(this.getSelected());
		}
	}

	// 在光标处粘贴
	public void paste() {
		String str = getClipboard();
		this.content = this.content.substring(0, this.cursorI) + str + this.content.substring(this.cursorI);
		this.setCursorIndex(this.cursorI + str.length());
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

	// 设置光标索引
	private void setCursorIndex(int i) {
		i = Math.max(0, Math.min(this.content.length(), i));
		this.cursorI = i;
		int[] cc = this.getIndexCoord(i);
		this.cursorX = cc[0];
		this.cursorY = cc[1];
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
