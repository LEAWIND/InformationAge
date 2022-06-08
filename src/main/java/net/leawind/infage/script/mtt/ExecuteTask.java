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
			if (deviceObj.storage == null)
				this.that.storage = "";
			else
				this.that.storage = deviceObj.storage.length() > this.that.storageSize ? //
						deviceObj.storage.substring(0, this.that.storageSize) : //
						deviceObj.storage;

			// 将 deviceObj 的发送缓存 覆盖到 that 的发送缓存
			if (deviceObj.dataToSend == null) {
				Arrays.fill(this.that.sendCaches, null);
			} else {
				this.that.sendCaches = Arrays.copyOfRange(deviceObj.dataToSend, 0, this.that.portsCount);
			}
		} catch (ScriptException e) { // 脚本执行出错
			// TODO 将错误输出
		}
	}
}
