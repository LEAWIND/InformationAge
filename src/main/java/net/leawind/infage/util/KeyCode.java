package net.leawind.infage.util;

public final class KeyCode {
	public static final int SPACE;
	public static final int ESC;
	public static final int ENTER;
	public static final int TAB;
	public static final int BACKSPACE;
	public static final int DELETE;
	public static final int RIGHT;
	public static final int LEFT;
	public static final int UP;
	public static final int PAGE_UP;
	public static final int PAGE_DOWN;
	public static final int HOME;
	public static final int END;
	public static final int CAPSLOCK;
	public static final int NUMLOCK;
	public static final int DOWN;
	public static final int LSHIFT;
	public static final int LCTRL;
	public static final int LALT;
	public static final int WIN;
	public static final int RSHIFT;
	public static final int RCTRL;
	public static final int RALT;

	static {
		SPACE = 32;
		ESC = 256;
		ENTER = 257;
		TAB = 258;
		BACKSPACE = 259;
		DELETE = 261;
		RIGHT = 262;
		LEFT = 263;
		UP = 265;
		PAGE_UP = 266;
		PAGE_DOWN = 267;
		HOME = 268;
		END = 269;
		CAPSLOCK = 280;
		NUMLOCK = 282;
		DOWN = 336;
		LSHIFT = 340;
		LCTRL = 341;
		LALT = 342;
		WIN = 343;
		RSHIFT = 344;
		RCTRL = 345;
		RALT = 346;
	}

	public static final int Letter(char c) {
		return (int) c;
	}

	public static final int Number(int i) {
		return 48 + i;
	}
}
