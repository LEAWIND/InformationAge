package net.leawind.universe.mttv3;

import java.util.ArrayDeque;
import java.util.Iterator;

// 备用线程
public class MTManager extends Thread {
	protected volatile int threadCount = 4;
	protected volatile int spareThreadCount = 2;
	public volatile int taskCountLimit = 1000;
	protected volatile ArrayDeque<MTThread> spareThreads = new ArrayDeque<MTThread>();
	protected volatile ArrayDeque<MTThread> threads = new ArrayDeque<MTThread>();
	public volatile ArrayDeque<MTTask> tasks = new ArrayDeque<MTTask>();
	protected volatile boolean shouldCheckThreadCount = true;

	public MTManager() {
		this(4);
	}

	public MTManager(int threadCount) {
		this(threadCount, 2);
	}

	public MTManager(int threadCount, int spareThreadCount) {
		this.setThreadCount(threadCount);
		this.setSpareThreadCount(spareThreadCount);
		this.setName("MTM-Main-" + this.getId());
		this.start();
	}

	public int getThreadCount() {
		return this.threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
		this.shouldCheckThreadCount = true;
	}

	public int getSpareThreadCount() {
		return this.spareThreadCount;
	}

	public void setSpareThreadCount(int spareThreadCount) {
		this.spareThreadCount = spareThreadCount;
		this.shouldCheckThreadCount = true;
	}

	public int getTaskCount() {
		synchronized (this.tasks) {
			return this.tasks.size();
		}
	}

	protected synchronized void checkThreadCount() {
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
				thread.setName("MTM-Thread-" + thread.getId());
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

	public void addTask(MTTask task) {
		synchronized (this.tasks) {
			if (this.tasks.size() < this.taskCountLimit) {
				task.init();
				this.tasks.add(task);
			} else {
				System.out.println("Error: task count limit exceeded: " + this.taskCountLimit);
			}
		}
	}

	public MTTask popTask() {
		synchronized (this.tasks) {
			return this.tasks.size() > 0 ? this.tasks.pop() : null;
		}
	}

	public int clearTasks() {
		int i;
		synchronized (this.tasks) {
			i = this.tasks.size();
			this.tasks.clear();
		}
		return i;
	}

	public void interruptAllThreads() {
		synchronized (this.threads) {
			for (MTThread thread : this.threads)
				thread.interrupt();
		}
	}

	@SuppressWarnings("deprecation")
	public int stopAllThreads() {
		int i;
		synchronized (this.threads) {
			i = this.threads.size();
			for (MTThread thread : this.threads)
				thread.stop();
			this.threads.clear();
		}
		this.shouldCheckThreadCount = true;
		return i;
	}

	@SuppressWarnings("deprecation")
	public int stopThreads(MTState state) {
		int i = 0;
		synchronized (this.threads) {
			Iterator<MTThread> iterator = this.threads.iterator();
			while (iterator.hasNext()) {
				MTThread thread = iterator.next();
				synchronized (thread) {
					if (thread.state == state) {
						iterator.remove();
						thread.stop();
						i++;
					}
				}
			}
		}
		this.shouldCheckThreadCount = true;
		return i;
	}

	public boolean isAllTaskFinished() {
		synchronized (this.threads) {
			for (MTThread thread : this.threads) {
				synchronized (thread) {
					if (thread.state == MTState.EXECING)
						return false;
				}
			}
			return true;
		}
	}

	public ArrayDeque<MTThread> getThreadsDeque() {
		return this.threads;
	}

	public static void println(Object... objs) {
		for (Object obj : objs)
			synchronized (System.out) {
				System.out.println(obj);
			}
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			if (this.shouldCheckThreadCount)
				this.checkThreadCount();
			try {
				sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.shouldCheckThreadCount = false;
		this.tasks.clear();
		for (Thread thread : this.threads)
			thread.interrupt();
		for (Thread thread : this.spareThreads)
			thread.interrupt();
		this.interrupt();
	}

	public void close() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public class MTThread extends Thread {
		protected volatile MTManager manager;
		protected volatile boolean shouldPause = false;
		public volatile MTState state = MTState.RESTING;
		public volatile MTTask task = null;

		public MTThread(MTManager mtm, boolean shouldPause) {
			super();
			this.manager = mtm;
			this.shouldPause = shouldPause;
		}

		public void pause() {
			this.shouldPause = true;
		}

		public void goOn() {
			this.shouldPause = false;
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				if (this.shouldPause) {
					try {
						sleep(0);
					} catch (InterruptedException e) {
						break;
					}
				} else {
					this.task = this.manager.popTask();
					if (this.task != null) {
						this.state = MTState.EXECING;
						synchronized (this.task) {
							this.task.thread = this;
							try {
								this.task.execute();
							} catch (Exception e) {
								this.task.exceptionHandler(e);
							}
							this.task = null;
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
