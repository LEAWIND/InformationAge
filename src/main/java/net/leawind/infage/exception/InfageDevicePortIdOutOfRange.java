package net.leawind.infage.exception;

public class InfageDevicePortIdOutOfRange extends Exception {
	int portsCount;
	int portId;

	public InfageDevicePortIdOutOfRange(int portsCount, int portId) {
		this.portsCount = portsCount;
		this.portId = portId;
	}
}
