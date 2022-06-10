package net.leawind.infage.util;

import java.util.Arrays;

public class Others {
	public static byte[] arrayFrom(byte[] arr, int len) {
		if (arr == null) {
			return new byte[len];
		} else {
			int leng = arr.length;
			byte[] res = new byte[len];
			if (leng < len) {
				Arrays.fill(res, leng, len, (byte) 0);
				System.arraycopy(arr, 0, res, 0, leng);
			} else {
				System.arraycopy(arr, 0, res, 0, len);
			}
			return res;
		}
	}

	public static int[] arrayFrom(int[] arr, int len) {
		if (arr == null) {
			return new int[len];
		} else {
			int leng = arr.length;
			int[] res = new int[len];
			if (leng < len) {
				Arrays.fill(res, leng, len, 0);
				System.arraycopy(arr, 0, res, 0, leng);
			} else {
				System.arraycopy(arr, 0, res, 0, len);
			}
			return res;
		}
	}

	public static long[] arrayFrom(long[] arr, int len) {
		if (arr == null) {
			return new long[len];
		} else {
			int leng = arr.length;
			long[] res = new long[len];
			if (leng < len) {
				Arrays.fill(res, leng, len, 0L);
				System.arraycopy(arr, 0, res, 0, leng);
			} else {
				System.arraycopy(arr, 0, res, 0, len);
			}
			return res;
		}
	}

	public static String[] arrayFrom(String[] arr, int len) {
		String[] res;
		res = new String[len];
		if (arr == null) {
			Arrays.fill(res, "");
		} else {
			int leng = arr.length;
			if (leng < len) {
				Arrays.fill(res, leng, len, "");
				System.arraycopy(arr, 0, res, 0, leng);
			} else {
				System.arraycopy(arr, 0, res, 0, len);
			}
		}
		return res;
	}
}
