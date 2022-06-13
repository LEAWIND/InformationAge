package net.leawind.infage.script;

public enum CompileState {
	UNKNOWN, // 新的脚本还没编译
	SUCCESS, // 编译成功
	DISTRIBUTED, // 已经发布编译任务，但还没编译完成
	ERROR,// 编译出错
}
