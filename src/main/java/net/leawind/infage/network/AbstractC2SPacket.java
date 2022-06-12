package net.leawind.infage.network;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractC2SPacket implements Packet<ServerPlayPacketListener> {
	protected List<Object> objs;

	@Environment(EnvType.CLIENT)
	public AbstractC2SPacket() {}

	@Environment(EnvType.CLIENT)
	public AbstractC2SPacket(Object... objs) {
		for (Object obj : objs)
			this.objs.add(obj);
	}

	@Override
	public void apply(ServerPlayPacketListener listener) {
		// TODO
	}

	@Override
	public void read(PacketByteBuf buf) throws IOException {

	}

	@Override
	public void write(PacketByteBuf buf) throws IOException {
		for (Object obj : objs)
			writeObj(buf, obj);
	}

	public void writeObj(PacketByteBuf buf, Object x) throws IOException {
		if (x == null) {

		} else if (x instanceof Byte) {
			buf.writeByte((byte) x);
		} else if (x instanceof byte[]) {
			buf.writeByteArray((byte[]) x);

		} else if (x instanceof Short) {
			buf.writeShort((Short) x);

		} else if (x instanceof Integer) {
			buf.writeInt((int) x);
		} else if (x instanceof Integer[]) {
			buf.writeIntArray((int[]) x);

		} else if (x instanceof Long) {
			buf.writeLong((long) x);
		} else if (x instanceof Long[]) {
			buf.writeLongArray((long[]) x);

		} else if (x instanceof Float) {
			buf.writeFloat((Float) x);

		} else if (x instanceof Double) {
			buf.writeDouble((Double) x);

		} else if (x instanceof String) {
			buf.writeString((String) x);
		} else if (x instanceof Boolean) {
			buf.writeBoolean((Boolean) x);

		} else if (x instanceof BlockPos) {
			buf.writeBlockPos((BlockPos) x);

		} else if (x instanceof Enum<?>) {
			buf.writeEnumConstant((Enum<?>) x);

		} else if (x instanceof UUID) {
			buf.writeUuid((UUID) x);

		} else if (x instanceof CompoundTag) {
			buf.writeCompoundTag((CompoundTag) x);

		} else if (x instanceof ItemStack) {
			buf.writeItemStack((ItemStack) x);

		} else if (x instanceof Identifier) {
			buf.writeIdentifier((Identifier) x);

		} else if (x instanceof Date) {
			buf.writeDate((Date) x);

		} else if (x instanceof BlockHitResult) {
			buf.writeBlockHitResult((BlockHitResult) x);

		} else if (x instanceof ByteBuf) {
			buf.writeBytes((ByteBuf) x);

		} else if (x instanceof Text) {
			buf.writeText((Text) x);

		}
	}


}
