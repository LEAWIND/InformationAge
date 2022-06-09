package net.leawind.infage.script.mtt;

import java.util.Arrays;
import javax.script.Bindings;
import javax.script.ScriptException;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.universe.mttv1.MTTask;

public class ExecuteTask extends MTTask {
	public byte weight = 10;
	public DeviceEntity that;
	public Bindings bindings;
	public DeviceObj deviceObj;

	public ExecuteTask() {}

	public ExecuteTask(DeviceEntity deviceEntity) {
		this.that = deviceEntity;
	}

	@Override
	public void taskBody() {
		try {
			this.that.compiledScript_tick.eval(this.bindings);
			// 脚本执行成功
			Arrays.fill(this.that.sendCaches, ""); // 清空接收缓存
			this.that.applyObj(deviceObj); // 将脚本对 obj 做的修改 应用到 方块实体
		} catch (ScriptException e) {
			// 脚本执行出错
			this.that.writeLog("ExecuteTask", "Exception:\n" + e);
		}
	}
}
