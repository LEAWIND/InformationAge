package net.leawind.universe.mttv2;

public class MTTest {
	public static int maxNumber = (int) 1e6;
	public static int taskCount = 100;

	public static void main(String[] args) {
		int step = maxNumber / taskCount;
		boolean[] pmap = new boolean[maxNumber - 1];

		MTManager mtm = new MTManager();
		mtm.setThreadCount(16);
		mtm.setSpareThreadCount(5);

		for (int i = 0; i < maxNumber; i += step) {
			TestTask t = new TestTask(pmap, i, i + step);
			mtm.addTask(t);
		}

		int j = 0;
		try {
			while (true) {
				if (j % 6 == 1) {
					int sc = mtm.stopAllThreads();
					MTManager.println("Stopped " + sc + " threads");
				}

				MTManager.println("main: j=" + j++);
				Thread.sleep(128);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class TestTask extends MTTask {
		public volatile boolean[] pmap;
		public int r0, r1;
		boolean isDone = false;

		public TestTask(boolean[] pmap, int r0, int r1) {
			super();
			this.pmap = pmap;
			this.r0 = r0;
			this.r1 = r1;
		}

		@Override
		public void taskBody() {
			for (int i = r0; i < r1; i++) {
				boolean res = true;
				if (i < 4) {
					res = false;
				} else {
					for (int j = 2; j < i / 2; j++) {
						if (i % j == 0) {
							res = false;
							break;
						}
					}
				}
				this.pmap[i] = res;
			}
			MTManager.println("task: [" + this.r0 + "," + this.r1 + ") OK");
			this.isDone = true;
		}
	}


}
