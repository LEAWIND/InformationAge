package net.leawind.infage.exception;

public class InfageDevicePortsNotMatchException extends Exception {
	int portsCount;

	public InfageDevicePortsNotMatchException(int portsCount) {
		this.portsCount = portsCount;
	}
}
