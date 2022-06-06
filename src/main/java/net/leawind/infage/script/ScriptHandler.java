// [Java Platform, Standard Edition Nashorn User's Guide]
// (https://docs.oracle.com/javase/10/nashorn/introduction.htm#JSNUG136)
// []()

package net.leawind.infage.script;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScriptHandler {
	private static final Logger LOGGER;
	public static ScriptEngine ENGINE;
	public static Bindings BINDINGS;
	public static final String[] DELETED_VARS;

	static {
		DELETED_VARS = new String[] {"java", "javax", "Java", "print"};
		LOGGER = LogManager.getLogger("ScriptHandler");
		ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
		try {
			String scr = ";";
			for (String varn : DELETED_VARS)
				scr += varn + "=undefined;";
			ENGINE.eval(scr);
		} catch (ScriptException e) {
			System.out.println("这也能出错？？？");
			System.out.println(e);
		}

		BINDINGS = ENGINE.createBindings();
	}

	public static void test() {
		try {
			ENGINE.eval("print('Hello World!');");
		} catch (Exception e) {
			LOGGER.info(e);
		}
	}

	/**
	 * 将脚本包装成一个 立即调用的匿名函数，使脚本运行在一个独立的作用域内
	 */
	public static String packScript(String script) {
		return "(function(){" + script + "})();";
	}

	/**
	 * 编译脚本
	 */
	public static CompiledScript compile(String scriptText) {
		try {
			return ((Compilable) ENGINE).compile(packScript(scriptText));
		} catch (ScriptException e) {
			return null;
		}
	}

	// 执行已编译的脚本
	public static Object execute(CompiledScript cs) {
		try {
			cs.eval(BINDINGS);
		} catch (ScriptException e) {
		}
		return BINDINGS;
	}
}
