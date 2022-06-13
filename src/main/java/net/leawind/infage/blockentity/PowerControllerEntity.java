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
		// 更新比较器输出
		boolean doUpdateComparators = this.powerLevel != ((PowerControllerObj) obj).powerLevel;
		this.powerLevel = ((PowerControllerObj) obj).powerLevel;
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
