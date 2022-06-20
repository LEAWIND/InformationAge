package net.leawind.universe.mttv2;

public abstract class MTTask {
	private volatile boolean isFinished = false; // 是否已结束
	private volatile boolean isPerforming = false; // 是否正在执行

	public final void init() {
		this.isFinished = false;
		this.isPerforming = false;
	}

	public MTTask() {}

	public final void execute() {
		assert (!this.isPerforming);
		this.isPerforming = true;
		this.taskBody();
		this.isPerforming = false;
		this.isFinished = true;
	}

	public boolean isFinished() {
		return this.isFinished;
	}

	public boolean isPerforming() {
		return this.isPerforming;
	}

	public void exceptionHandler(Throwable t) {
		MTManager.println(t);
	}

	public abstract void taskBody();

	// TODO
	public static class TaskForcedStoppedException extends Exception {
		public TaskForcedStoppedException() {

		}
	}
}
