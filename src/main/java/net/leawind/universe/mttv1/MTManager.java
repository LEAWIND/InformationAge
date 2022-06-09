package net.leawind.universe.mttv1;

import java.util.ArrayDeque;
import java.util.Iterator;

public class MTManager {
	protected ArrayDeque<MTThread> threads = new ArrayDeque<MTThread>(); // 执行任务的线程
	public ArrayDeque<MTTask> tasks = new ArrayDeque<MTTask>(); // 待处理的任务

	protected int threadCount;

	public MTManager() {
		this.threadCount = 2;
		this.checkThreadCount();
	}

	public MTManager(int threadCount) {
		this.threadCount = threadCount;
		this.checkThreadCount();
	}

	public synchronized int getThreadCount() {
		return this.threadCount;
	}

	public synchronized int getTaskCount() {
		return this.tasks.size();
	}

	// 确保实际线程数量与预期数量一致
	public synchronized void checkThreadCount() {
		int leng = this.threads.size() - this.threadCount;
		if (leng > 0) {
			Iterator<MTThread> iterator = this.threads.iterator(); // 获取线程迭代器
			while (leng > 0 && iterator.hasNext()) {
				MTThread thread = (MTThread) iterator.next();
				thread.stopWhenIdle();// 告诉它停下来
				iterator.remove();// 从列表里删除
				leng--;
			}
		}
		while (leng < 0) {
			MTThread thread = new MTThread();
			thread.mtm = this;
			this.threads.add(thread);
			thread.start();
			leng++;

		}
	}

	// 添加任务
	public synchronized void addTask(MTTask task) {
		task.init();
		this.tasks.add(task);
	}

	public synchronized MTTask getTask() {
		if (this.tasks.size() > 0)
			return this.tasks.pop();
		else
			return null;
	}

	// 清除所有待处理任务
	public synchronized int clearTasks() {
		int i = this.tasks.size();
		this.tasks.clear();
		return i;
	}

	// 中断所有线程
	public synchronized void interruptAll() {
		Iterator<MTThread> iterator = this.threads.iterator(); // 获取线程迭代器
		while (iterator.hasNext()) {
			MTThread thread = (MTThread) iterator.next();
			thread.interrupt();
		}
	}

	public synchronized long getTotalAdder() {
		long s = 0;
		Iterator<MTThread> iterator = this.threads.iterator(); // 获取线程迭代器
		while (iterator.hasNext()) {
			s += iterator.next().adder;
		}
		return s;
	}
}
