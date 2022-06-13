# 信息时代 Imformation Age

## Micrrosoft TODO

[Infage-Coding-TODO](https://to-do.microsoft.com/sharing?InvitationToken=zx3BC6vPymMs1j5MUjTxywB1fx2pGZZ1jwyUpQ4o5-ycMuWgondLm8ObjXHobP5EA)



定义了一些新方块（网络设备）。

各种网络设备之间可以互相通信，网络设备和世界交互（通过比较器输出、监测充能状态、消化和生成物品等）。

消化和生成物品机制不允许无限生产物品，但它们的特性为远距离快速传送物品提供了一种新的方式。

### 机制

#### 接口 ports

任何设备都有至少1个接口。
通过接口可以与有限距离内的其他设备相连。

#### tick 事件脚本

每tick会执行一次自己的 `tick`脚本。

在脚本中可以设置在这一周期后要发送的数据。

脚本语言是 Javascript，当前在 1.16.5 使用的引擎是 Nashorn ，在更新到 1.19 以上后可能会使用性能更好的 graal 引擎。

#### 存储 storage

在脚本中可以通过 storage 来永久存储字符串数据。这些数据会保存在方块的 NBT 标签中。

存储的字符串长度是有限的。

### 计算机

`net.leawind.infage.block.Computer`
在计算机的脚本中，可以做一些较为“聪明”的操作，例如判断物品令牌对应的物品类型，更新物品令牌。

### 交换机

`net.leawind.infage.block.Switch`

它可以实现现实中交换机或路由器的功能（取决于你为它配置的脚本）

### 充能传感器

`net.leawind.infage.block.PowerSensor`

可以在脚本中获取方块的充能状态

###  充能控制器

`net.leawind.infage.block.PowerController`

可以控制这个方块 通过比较器输出红石信号 的强度。

### 物品消化器

`net.leawind.infage.block.ItemRegester`

物品消化器可以将物品兑换为一个 物品令牌。

物品令牌是一个长度为 64 的无规则`byte[]`类型数据(64Byte)

$$
\begin{align}
n &= 2 ^ {8\times64} = 2^{512}\\
&= 13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084096 \\
&\approx 1.34078 \times 10^{154}
\end{align}
$$
碰撞几率可以忽略不计。

### 物品生成器

`net.leawind.infage.block.ItemGenerator`

物品生成器可以将物品令牌兑换成相应的物品，随后这个令牌就会立即失效。

### 磁盘组

`net.leawind.infage.block.Disk`

磁盘组唯一的特点就是可以存储较大量的数据。

## 物品

以上所有方块都有与之对应的物品，<del>除此之外，还有一些其他物品。</del>

