package net.leawind.universe.mttv1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// 多线程任务管理器
public class MTManager {
	protected List<MTThread> threads = new ArrayList<MTThread>(); // 执行任务的线程
	public LinkedList<MTTask> tasks = new LinkedList<MTTask>(); // 待处理的任务
	// 链表
	// pop 弹出第一个元素
	// add 在末尾加入 1 个元素

	public MTManager() {
		this.setThreadCount(1);
	}

	public MTManager(int threadCount) {
		this.setThreadCount(threadCount);
	}

	public int getThreadCount() {
		return this.threads.size();
	}

	public int getTaskCount() {
		return this.tasks.size();
	}

	public void setThreadCount(int n) {
		int leng = this.threads.size() - n;
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

	public void addTask(MTTask task) {
		task.init();
		int i = 0;
		Iterator<MTTask> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			MTTask task1 = iterator.next();
			if (task1.weight <= task.weight) {
				this.tasks.add(i, task);
				break;
			}
			i++;
		}
	}

	public void clearTask() {
		this.tasks.clear();
	}

	public void clearByWeight(byte w0, byte w1) {
		Iterator<MTTask> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			MTTask task = iterator.next();
			if (task.weight >= w0 && task.weight < w1)
				iterator.remove();
		}
	}

	public int clearByWeight(byte w0, byte w1, boolean getKills) {
		if (!getKills) {
			this.clearByWeight(w0, w1);
			return 0;
		}
		int k = 0;
		Iterator<MTTask> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			MTTask task = iterator.next();
			if (task.weight >= w0 && task.weight < w1) {
				k++;
				iterator.remove();
			}
		}
		return k;
	}

	public long getTotalAdder() {
		long s = 0;
		Iterator<MTThread> iterator = this.threads.iterator(); // 获取线程迭代器
		while (iterator.hasNext()) {
			s += iterator.next().adder;
		}
		return s;
	}
}
