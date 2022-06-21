package net.leawind.infage.script;

import net.leawind.universe.mttv3.MTManager;

public class ExecThreadManager extends MTManager {
	public ExecThreadManager() {
		this(4);
	}

	public ExecThreadManager(int threadCount) {
		this(threadCount, 3);
	}

	public ExecThreadManager(int threadCount, int spareThreadCount) {
		this.setThreadCount(threadCount);
		this.setSpareThreadCount(spareThreadCount);
	}

	public class MTThread extends MTManager.MTThread {
		public MTThread(MTManager mtm, boolean shouldPause) {
			super(mtm, shouldPause);
		}
	}
}
