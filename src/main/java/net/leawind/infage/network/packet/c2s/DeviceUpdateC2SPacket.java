package net.leawind.infage.network.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.leawind.infage.blockentity.DeviceEntity;
import net.leawind.infage.blockentity.PowerControllerEntity;
import net.leawind.infage.client.gui.screen.InfageDeviceScreen;
import net.leawind.infage.settings.InfageSettings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class DeviceUpdateC2SPacket extends AbstractC2SPacket {
	public static final Identifier PACKET_ID = new Identifier(InfageSettings.NAMESPACE, "update_device");

	@Override
	public Identifier getId() {
		return PACKET_ID;
	}

	public DeviceUpdateC2SPacket(BlockPos pos, DeviceEntity.Action action, Object... args) {
		this.writeBlockPos(pos);
		this.writeEnumConstant(action);
		switch (action) {
			case GET_ALL_DATA: // 请求获取最新数据
				break;
			case RQ_BOOT: // 启动设备
				break;
			case RQ_SHUT_DOWN: // 关闭设备
				break;
			case RQ_DISCONNECT: // 断开指定端口
				this.writeByte((int) args[0]); // B 端口号
				break;
			case RQ_CONNECT: // 玩家希望连接指定端口
				this.writeByte((int) args[0]); // B 端口号
				break;
			case RQ_LOCK_PORT: // 锁定端口
				this.writeByte((int) args[0]); // B 端口号
				break;
			case RQ_UNLOCK_PORT: // 解锁端口
				this.writeByte((int) args[0]); // B 端口号
				break;
			case PUSH_ALL_DATA: // 更新全部数据
				((InfageDeviceScreen) args[0]).writeAllDataToBuf(this);
				break;
			case PUSH_SCRIPT: // 更新脚本
				this.writeString((String) args[0]);
				break;
			case DRINK_A_CUP_OF_TEA:
				break;
		}
	}

	public static void apply(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		PacketByteBuf bufCopy = PacketByteBufs.create();
		bufCopy.writeBytes(buf.copy()); // 复制一份，以便在匿名函数中使用
		ServerWorld world = player.getServerWorld();
		BlockPos pos = bufCopy.readBlockPos();
		DeviceEntity.Action action = bufCopy.readEnumConstant(DeviceEntity.Action.class);

		server.execute(() -> {
			BlockEntity blockEntity = world.getBlockEntity(pos); // 获取方块实体
			if (!(blockEntity instanceof DeviceEntity))
				return;
			DeviceEntity device = (DeviceEntity) blockEntity;
			int portId;
			switch (action) {
				case GET_ALL_DATA: // TODO GET ALL DATA
					break;
				case RQ_BOOT:
					device.device_boot();
					break;
				case RQ_SHUT_DOWN:
					device.device_shutdown();
					break;
				case RQ_DISCONNECT:
					portId = bufCopy.readByte();
					device.disconnect(portId, false);
					break;
				case RQ_CONNECT:
					portId = bufCopy.readByte();
					// 将玩家连接至指定端口
					device.onPlayerClickPort(portId, player);
					break;
				case RQ_LOCK_PORT:
					portId = bufCopy.readByte();
					device.setPortState(portId, DeviceEntity.PortState.CONNECT_LOCKED);
					break;
				case RQ_UNLOCK_PORT:
					portId = bufCopy.readByte();
					device.setPortState(portId, DeviceEntity.PortState.CONNECT_UNLOCKED);
					break;
				case PUSH_ALL_DATA: // 全部数据
					if (bufCopy.readBoolean()) { // 开机状态
						device.device_boot();
					} else { // 关机状态
						device.device_shutdown();
					}
					device.setScirpt(bufCopy.readString());
					break;
				case PUSH_SCRIPT:
					device.setScirpt(bufCopy.readString());
					break;
				case DRINK_A_CUP_OF_TEA:
					break;
			}
			if (device instanceof PowerControllerEntity) {
				((PowerControllerEntity) device).updateComparators();
			}
		});
	}
}
