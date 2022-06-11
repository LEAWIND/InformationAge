// [Java Platform, Standard Edition Nashorn User's Guide]
// (https://docs.oracle.com/javase/10/nashorn/introduction.htm#JSNUG136)
// []()

package net.leawind.infage.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.universe.mttv1.MTManager;

public final class ScriptHelper {
	private static final Logger LOGGER;
	public static ScriptEngine ENGINE; // 脚本引擎
	public static final MTManager MTM_EXEC;
	public static final MTManager MTM_COMPILE;
	public static final String[] DELETED_VARS;

	static {
		LOGGER = LogManager.getLogger("InfageScriptHandler");
		DELETED_VARS = new String[] {"java", "javax", "Java", "exit", "quit"}; // 禁止在脚本中访问的全局对象
		ENGINE = new ScriptEngineManager().getEngineByName("nashorn"); // 获取 nashorn 引擎
		MTM_EXEC = new MTManager(Runtime.getRuntime().availableProcessors() / 2 + 1); // 执行线程数是 CPU 核心数 / 2 + 1
		MTM_COMPILE = new MTManager(3); // 编译线程们

		try {
			String scr = ";";
			for (String varn : DELETED_VARS)
				scr += varn + "=undefined;";
			ENGINE.eval(scr);
		} catch (ScriptException e) {
			System.out.println("这也能出错？？？");
			System.out.println(e);
		}

		LOGGER.info("Infage ScriptHandler is loaded.");
	}

	// 将脚本包装成一个 立即调用的匿名函数，使脚本运行在一个独立的作用域内
	public static final String packScript(String script) {
		return "(function(){" + script + "})();";
	}

	// 编译脚本
	public static CompiledScript compile(String scriptText) throws ScriptException {
		return ((Compilable) ENGINE).compile(packScript(scriptText));
	}

	public synchronized static void errorLog(Object obj) {
		LOGGER.error(obj);
	}

	public synchronized static void warnLog(Object obj) {
		LOGGER.warn(obj);
	}

	public synchronized static void infoLog(Object obj) {
		LOGGER.info(obj);
	}
}
