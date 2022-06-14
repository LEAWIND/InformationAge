package net.leawind.infage.util;

public final class KeyCode {
	public static final int SPACE = 32;
	public static final int ESC = 256;
	public static final int ENTER = 257;
	public static final int TAB = 258;
	public static final int BACKSPACE = 259;
	public static final int DELETE = 261;
	public static final int RIGHT = 262;
	public static final int LEFT = 263;
	public static final int UP = 265;
	public static final int PAGE_UP = 266;
	public static final int PAGE_DOWN = 267;
	public static final int HOME = 268;
	public static final int END = 269;
	public static final int CAPSLOCK = 280;
	public static final int NUMLOCK = 282;
	public static final int DOWN = 336;
	public static final int LSHIFT = 340;
	public static final int LCTRL = 341;
	public static final int LALT = 342;
	public static final int WIN = 343;
	public static final int RSHIFT = 344;
	public static final int RCTRL = 345;
	public static final int RALT = 346;

	public static final int Letter(char c) {
		return (int) c;
	}

	public static final int Number(int i) {
		return 48 + i;
	}
}
