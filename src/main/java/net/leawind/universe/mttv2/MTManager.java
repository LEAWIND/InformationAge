package net.leawind.universe.mttv2;

import java.util.ArrayDeque;
import java.util.Iterator;

// 备用线程
public class MTManager {
	protected int threadCount = 4;
	protected int spareThreadCount = 2; // 备用线程数量
	protected ArrayDeque<MTThread> spareThreads = new ArrayDeque<MTThread>(); // 备用线程
	protected ArrayDeque<MTThread> threads = new ArrayDeque<MTThread>(); // 执行任务的线程
	public ArrayDeque<MTTask> tasks = new ArrayDeque<MTTask>(); // 待处理的任务

	public MTManager() {
		this(2);
	}

	public MTManager(int threadCount) {
		this(threadCount, 3);
	}

	public MTManager(int threadCount, int spareThreadCount) {
		this.setThreadCount(threadCount);
		this.setSpareThreadCount(spareThreadCount);
	}

	public synchronized int getThreadCount() {
		return this.threadCount;
	}

	public synchronized void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
		this.checkThreadCount();
	}

	public synchronized int getSpareThreadCount() {
		return this.spareThreadCount;
	}

	public synchronized void setSpareThreadCount(int spareThreadCount) {
		this.spareThreadCount = spareThreadCount;
		this.checkThreadCount();
	}

	public synchronized int getTaskCount() {
		return this.tasks.size();
	}

	private synchronized void checkThreadCount() {
		if (this.threads.size() > this.threadCount) {
			Iterator<MTThread> iterator = this.threads.iterator();
			while ((this.threads.size() > this.threadCount) && iterator.hasNext()) {
				MTThread thread = (MTThread) iterator.next();
				thread.interrupt();
				iterator.remove();
				this.spareThreads.add(thread);
			}
		} else {
			int targetTotalCount = this.spareThreadCount + this.threadCount;
			while (this.spareThreads.size() + this.threads.size() < targetTotalCount) {
				MTThread thread = new MTThread(this, true);
				thread.start();
				this.spareThreads.add(thread);
			}
			while (this.threads.size() < this.threadCount && !this.spareThreads.isEmpty()) {
				MTThread thread = this.spareThreads.pop();
				thread.goOn();
				this.threads.add(thread);
			}
		}
		return;
	}

	public synchronized void addTask(MTTask task) {
		task.init();
		this.tasks.add(task);
	}

	public synchronized MTTask popTask() {
		return this.tasks.size() > 0 ? this.tasks.pop() : null;
	}

	public synchronized int clearTasks() {
		int i = this.tasks.size();
		this.tasks.clear();
		return i;
	}

	public synchronized void interruptAllThreads() {
		for (MTThread thread : this.threads)
			thread.interrupt();
	}

	@SuppressWarnings("deprecation")
	public synchronized int stopAllThreads() {
		int i = this.threads.size();
		for (MTThread thread : this.threads)
			thread.stop();
		this.threads.clear();
		this.checkThreadCount();
		return i;
	}

	@SuppressWarnings("deprecation")
	public synchronized int stopThreads(MTState state) {
		int i = 0;
		Iterator<MTThread> iterator = this.threads.iterator();
		while (iterator.hasNext()) {
			MTThread thread = iterator.next();
			if (thread.state == state) {
				thread.stop();
				iterator.remove();
				i++;
			}
		}
		this.checkThreadCount();
		return i;
	}

	public boolean isAllTaskFinished() {
		for (MTThread thread : this.threads) {
			if (thread.state == MTState.EXECING)
				return false;
		}
		return true;
	}

	public ArrayDeque<MTThread> getThreadsDeque() {
		return this.threads;
	}

	public synchronized static void println(Object... objs) {
		for (Object obj : objs)
			System.out.println(obj);
	}

	private class MTThread extends Thread {
		private MTManager manager;
		private boolean shouldPause = false;
		public volatile MTState state = MTState.RESTING;

		public MTThread(MTManager mtm, boolean shouldPause) {
			super();
			this.manager = mtm;
			this.shouldPause = shouldPause;
		}

		public void goOn() {
			this.shouldPause = false;
		}

		@Override
		public synchronized void run() {
			MTTask task;
			while (!this.isInterrupted()) {
				if (this.shouldPause) {
					try {
						sleep(0);
					} catch (InterruptedException e) {
						break;
					}
				} else {
					task = this.manager.popTask();
					if (task != null) {
						this.state = MTState.EXECING;
						try {
							task.execute();
						} catch (Exception e) {
							task.exceptionHandler(e);
						}
						this.state = MTState.RESTING;
					}
				}
			}
		}
	}

	public static enum MTState {
		RESTING, EXECING,
	}

}
