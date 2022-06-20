package net.leawind.infage;

import java.util.UUID;
import net.minecraft.util.math.BlockPos;

public class PlayerData {
	public UUID uuid;
	private BlockPos selectedDevicePos = null;
	private byte selectedPortId = -1;

	public PlayerData(UUID uuid) {
		this.uuid = uuid;
	}

	public void setSelected(BlockPos pos, byte portId) {
		this.selectedDevicePos = pos;
		this.selectedPortId = portId;
	}

	public BlockPos getSelectedBlockPos() {
		return this.selectedDevicePos;
	}

	public byte getSelectedPortId() {
		return this.selectedPortId;
	}

}
