package net.leawind.infage.script.mtt;

import javax.script.ScriptException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.CompileState;
import net.leawind.infage.script.ScriptHelper;
import net.leawind.universe.mttv3.MTTask;

@Environment(EnvType.SERVER)
public class CompileTask extends MTTask {
	public DeviceEntity deviceEntity;

	public CompileTask(DeviceEntity deviceEntity) {
		this.deviceEntity = deviceEntity;
	}

	@Override
	public void taskBody() {
		this.deviceEntity.compileState = CompileState.DISTRIBUTED;
		try {
			this.deviceEntity.compiledScript = ScriptHelper.compile(this.deviceEntity.script);
			// 编译成功
			this.deviceEntity.compileState = CompileState.SUCCESS;
			this.deviceEntity.scriptTimeoutCounter = 0; // 重置脚本超时计数器
		} catch (ScriptException e) {
			// 编译失败
			this.deviceEntity.compileState = CompileState.ERROR;
			this.deviceEntity.writeLog("CompileTask", "Exception:\n" + e);
		}
	}
}
