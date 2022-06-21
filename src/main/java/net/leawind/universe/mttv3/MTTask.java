package net.leawind.universe.mttv3;

public abstract class MTTask {
	protected volatile boolean isFinished = false; // 是否已结束
	protected volatile boolean isPerforming = false; // 是否正在执行
	public volatile MTManager.MTThread thread = null;

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

	public abstract void taskBody();

	public void exceptionHandler(Throwable t) {
		MTManager.println(t);
	}


	// TODO
	public static class TaskForcedStoppedException extends Exception {
		public TaskForcedStoppedException() {}
	}
}
