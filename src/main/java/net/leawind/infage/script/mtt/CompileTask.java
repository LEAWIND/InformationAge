package net.leawind.infage.script.mtt;

import javax.script.ScriptException;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.universe.mttv1.MTTask;

public class CompileTask extends MTTask {
	public byte weight = 20;
	public DeviceEntity that;

	public CompileTask(DeviceEntity deviceEntity) {
		this.that = deviceEntity;
	}

	@Override
	public void taskBody() {
		// 编译
		this.that.isCompiling = true;
		try {
			this.that.compiledScript_tick = ScriptHelper.compile(this.that.script_tick);
			this.that.isCompiling = false;
		} catch (ScriptException e) {
			e.printStackTrace();
			// TODO 处理编译错误
		}
	}
}
