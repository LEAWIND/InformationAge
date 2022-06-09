package net.leawind.infage.script.mtt;

import java.util.Arrays;
import javax.script.Bindings;
import javax.script.ScriptException;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.DeviceObj;
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

			// 取 storage
			this.that.setStorage(deviceObj.storage);

			// 取控制台输出
			if (deviceObj.outputs != null)
				this.that.writeOutputs(deviceObj.outputs);

			// 取发送缓存
			this.that.setSendCaches(deviceObj.dataToSend);

		} catch (ScriptException e) { // 脚本执行出错
			this.that.writeLog("ExecuteTask", "Exception:\n" + e.toString());
		}
	}
}
