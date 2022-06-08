package net.leawind.universe.mttv1;

public class MTThread extends Thread {
	// private boolean isIdle = true;
	private boolean keepRunning = true;
	public MTManager mtm;
	public long adder = 0;

	@Override
	public void run() {
		while (this.keepRunning) {
			// System.out.printf("[thread-1] loop = %d \n", loopCount);
			try {
				// 领取任务
				MTTask task = this.mtm.tasks.pop(); // 执行任务
				task.execute();
				// this.adder += 1;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public void stopWhenIdle() {
		this.keepRunning = false;
	}

	public boolean hasBeenToldToStop() {
		return !this.keepRunning;
	}
}
