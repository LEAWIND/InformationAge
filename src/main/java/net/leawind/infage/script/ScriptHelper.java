// [Java Platform, Standard Edition Nashorn User's Guide]
// (https://docs.oracle.com/javase/10/nashorn/introduction.htm#JSNUG136)

package net.leawind.infage.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.leawind.infage.settings.InfageSettings;
import net.leawind.universe.mttv3.MTManager;

public final class ScriptHelper {
	private static final Logger LOGGER = LogManager.getLogger("InfageScriptHandler");
	public static final MTManager MTM_COMPILE = new MTManager(InfageSettings.COMPILE_THREAD_COUNT, 2); // 编译线程们;
	public static final ExecThreadManager MTM_EXEC = new ExecThreadManager(InfageSettings.EXEC_THREAD_COUNT, 5); // 执行线程数是 CPU 核心数 / 2 + 1;
	public static final String[] FORBIDDEN_VARS_IN_SCRIPT = new String[] {"java", "javax", "Java", "exit", "quit"}; // 禁止在脚本中访问的全局对象
	public static ScriptEngine ENGINE; // 脚本引擎
	public static int ENGINE_POOL_SIZE = 16;
	public static ScriptEngine[] ENGINE_POOL;

	static {
		ENGINE = createScriptEngine(); // 获取 nashorn 引擎
		ENGINE_POOL = new ScriptEngine[ENGINE_POOL_SIZE];
		for (int i = 0; i < ENGINE_POOL_SIZE; i++) {
			ENGINE_POOL[i] = createScriptEngine();
		}
	}

	// 创建引擎并配置好上下文
	public static ScriptEngine createScriptEngine() {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		try {
			String scr = ";";
			for (String varn : FORBIDDEN_VARS_IN_SCRIPT)
				scr += varn + "=undefined;";
			engine.eval(scr);
		} catch (ScriptException e) {
			System.out.println("这也能出错？？？");
			e.printStackTrace();
			return null;
		}
		return engine;
	}

	// 将脚本包装成一个 立即调用的匿名函数，使脚本运行在一个独立的作用域内
	public static final String makeScriptPrivate(String script) {
		return "(function(){" + script + "})();";
	}

	public static ScriptEngine randomlyGetScriptEngine() {
		return ENGINE_POOL[(int) (ENGINE_POOL_SIZE * Math.random())];
	}

	// Compile Script
	public static CompiledScript compile_r(String scriptText) throws ScriptException {
		ScriptEngine engine = randomlyGetScriptEngine();
		synchronized (engine) {
			return ((Compilable) ENGINE).compile(makeScriptPrivate(scriptText));
		}
	}

	public static CompiledScript compile(String scriptText) throws ScriptException {
		return ((Compilable) ENGINE).compile(makeScriptPrivate(scriptText));
	}

	public static CompiledScript compile(Compilable engine, String scriptText) throws ScriptException {
		return engine.compile(makeScriptPrivate(scriptText));
	}

	public static void errorLog(Object obj) {
		LOGGER.error(obj);
	}

	public static void warnLog(Object obj) {
		LOGGER.warn(obj);
	}

	public static void infoLog(Object obj) {
		synchronized (LOGGER) {
			LOGGER.info(obj);
		}
	}

}
