package net.leawind.infage.script.mtt;

import java.util.Arrays;
import javax.script.Bindings;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.universe.mttv3.MTTask;

public class ExecuteTask extends MTTask {
	public DeviceEntity deviceEntity;
	public Bindings bindings;
	public DeviceObj deviceObj;

	public ExecuteTask() {}

	public ExecuteTask(DeviceEntity deviceEntity) {
		this.deviceEntity = deviceEntity;
	}

	@Override
	public void taskBody() {
		boolean isSucceed = false;
		try {
			// 执行脚本
			this.deviceEntity.compiledScript.eval(this.bindings); // bindings 中有一个 deviceObj 对象可供脚本访问
			isSucceed = true;
		} catch (Exception e) {
			// 脚本执行出错
			this.deviceEntity.writeLog("ExecuteTask", "Exception:\n" + e);
		}
		if (isSucceed) {
			// 脚本执行成功
			Arrays.fill(this.deviceEntity.sendCaches, ""); // 清空接收缓存
			this.deviceEntity.applyObj(deviceObj); // 将脚本对 obj 做的修改 应用到 方块实体
		}
	}

	@Override
	public void exceptionHandler(Throwable t) {
		if (t instanceof TaskForcedStoppedException) {

		}
	}
}
