package net.leawind.universe.mttv1;

public abstract class MTTask {
	public byte weight = 0;
	public boolean isDistributed = false; // 是否已经被领取
	public boolean isFinished = false; // 是否已结束
	public boolean isPerforming = false; // 是否正在执行

	public MTTask() {
		this.weight = 0;
	}

	public MTTask(byte weight) {
		this.weight = weight;
	}

	public final void init() {
		this.isDistributed = false;
		this.isFinished = false;
		this.isPerforming = false;
	}

	// 执行任务
	public final void execute() {
		this.isPerforming = true;
		this.taskBody();
		this.isFinished = true;
		this.isPerforming = false;
	}

	public abstract void taskBody();
}