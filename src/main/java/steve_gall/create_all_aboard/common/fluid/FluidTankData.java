package steve_gall.create_all_aboard.common.fluid;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;

public record FluidTankData(@NotNull FluidStack stack, int capacity)
{
	public static FluidTankData fromNetwork(FriendlyByteBuf buffer)
	{
		FluidStack stack = FluidStack.readFromPacket(buffer);
		int capacity = buffer.readInt();
		return new FluidTankData(stack, capacity);
	}

	public static void toNetwork(FriendlyByteBuf buffer, FluidTankData tank)
	{
		tank.stack().writeToPacket(buffer);
		buffer.writeInt(tank.capacity());
	}

	public FluidTankData copy()
	{
		return new FluidTankData(this.stack().copy(), this.capacity());
	}

}
