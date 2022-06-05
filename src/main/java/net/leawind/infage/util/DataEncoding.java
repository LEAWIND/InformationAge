package net.leawind.infage.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataEncoding {
	public static final Logger LOGGER = LogManager.getLogger("StringEncoding");
	public static final String SPLITER = "I";
	public static final String ESCAPER = "-";
	public static final String ESCAPES = "/";

	public static final String encode_fake(String[] strArr) {
		String str = "";
		for (int i = 0; i < strArr.length; i++) {
			str += SPLITER;
		}
		return str;
	}

	public static final String[] decode_fake(String str) {
		if (str == null) {
			LOGGER.info("Arguement String str is NULL !!!");
		}
		String[] sa = new String[str.length()];
		for (int i = 0; i < str.length(); i++) {
			sa[i] = "";
		}
		return sa;
	}

	public static final String encode(String[] strArr) {
		if (strArr == null || strArr.length < 1)
			return SPLITER;
		String str;
		if (strArr[0] == null)
			strArr[0] = "";
		str = strArr[0]//
				.replaceAll(ESCAPES, ESCAPES + ESCAPER)//
				.replaceAll(SPLITER, ESCAPES + SPLITER);
		for (int i = 1; i < strArr.length; i++) {
			if (strArr[i] == null)
				strArr[i] = "";
			str += SPLITER + strArr[i]//
					.replaceAll(ESCAPES, ESCAPES + ESCAPER)//
					.replaceAll(SPLITER, ESCAPES + SPLITER);
		}
		return SPLITER + str;
	}

	public static final String[] decode(String str) {
		if (str == null) {
			LOGGER.info("Arguement String str is NULL !!!");
		}
		String[] strArr = str.split("(?<!" + ESCAPES + ")" + SPLITER, -1);
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i] == null) {
				strArr[i] = "";
			}
			strArr[i] = strArr[i]//
					.replace(ESCAPES + SPLITER, SPLITER)//
					.replace(ESCAPES + ESCAPER, ESCAPES)//
			;
		}
		// return strArr;
		String[] arr = new String[strArr.length - 1];
		System.arraycopy(strArr, 1, arr, 0, arr.length);
		return arr;
	}


	public static void printArr(String[] arr) {
		System.out.print("\n[\n");
		for (String s : arr)
			System.out.printf("\"%s\"\n", s);
		System.out.print("]\n");
	}

	public static void test() {
		// String[] sa = {"/3/", "I12", "---",};
		String[] sa = {};
		// String[] sa = {""};
		// String[] sa = {"", ""};
		// String[] sa = {"", "", "", "",};
		String tp = encode(sa);
		String[] sb = decode(tp);
		printArr(sa);
		System.out.println(tp);
		printArr(sb);
	}
}
