package net.leawind.infage.script.mtt;

import javax.script.ScriptException;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.CompileState;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.universe.mttv3.MTTask;

public class CompileTask extends MTTask {
	public DeviceEntity deviceEntity;

	public CompileTask(DeviceEntity deviceEntity) {
		this.deviceEntity = deviceEntity;
	}

	@Override
	public void taskBody() {
		// 编译
		this.deviceEntity.compileState = CompileState.DISTRIBUTED;
		try {
			this.deviceEntity.compiledScript = ScriptHelper.compile(this.deviceEntity.script);
			this.deviceEntity.compileState = CompileState.SUCCESS;
		} catch (ScriptException e) {
			this.deviceEntity.writeLog("CompileTask", "Exception:\n" + e);
			this.deviceEntity.compileState = CompileState.ERROR;
		}
	}
}
