package net.leawind.infage.blockentity;

import net.leawind.infage.registry.InfageBlockEntities;
import net.leawind.infage.registry.InfageBlocks;
import net.leawind.infage.script.obj.DeviceObj;
import net.leawind.infage.script.obj.PowerControllerObj;

public class PowerControllerEntity extends DeviceEntity {
	public int powerLevel = 0; //

	public PowerControllerEntity() {
		super(InfageBlockEntities.POWER_CONTROLLER);
		this.init();
	}

	@Override
	public void applyObj(DeviceObj obj) {
		super.applyObj(obj);
		PowerControllerObj pcobj = (PowerControllerObj) obj;
		pcobj.powerLevel = Math.max(1, Math.min(15, pcobj.powerLevel)); // 检查范围
		// 更新比较器输出
		this.powerLevel = pcobj.powerLevel;
		boolean doUpdateComparators = this.powerLevel != pcobj.powerLevel;
		if (doUpdateComparators)
			this.updateComparators();
	}

	@Override
	public DeviceObj getDeviceObj() {
		return new PowerControllerObj(this);
	}

	// 更新比较器输出
	public void updateComparators() {
		this.getWorld().updateComparators(this.getPos(), InfageBlocks.POWER_CONTROLLER);
	}
}
