package steve_gall.create_all_aboard.common.content.train;

import java.util.List;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class TrainPartsSyncData<PART extends TrainPart<?>>
{
	private int count = 0;
	private byte[] bytes = new byte[0];

	public TrainPartsSyncData()
	{

	}

	public void copy(TrainPartsSyncData<PART> from)
	{
		this.count = from.count;
		this.bytes = from.bytes.clone();
	}

	public void read(FriendlyByteBuf buffer)
	{
		this.count = buffer.readInt();
		this.bytes = buffer.readByteArray();
	}

	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.count);
		buffer.writeByteArray(this.bytes);
	}

	public void update(List<PART> parts)
	{
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

		for (TrainPart<?> part : parts)
		{
			part.writeSyncData(buffer);
		}

		this.count = parts.size();
		this.bytes = ByteBufUtil.getBytes(buffer);
	}

	public void apply(List<PART> parts)
	{
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(this.bytes));
		int size = Math.min(this.count, parts.size());

		for (int i = 0; i < size; i++)
		{
			parts.get(i).readSyncData(buffer);
		}

	}

}
