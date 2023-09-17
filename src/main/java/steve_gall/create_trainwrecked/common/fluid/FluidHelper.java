package steve_gall.create_trainwrecked.common.fluid;

import java.util.Collection;
import java.util.stream.Stream;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class FluidHelper
{
	public static FluidStack deriveAmount(FluidStack stack, int amount)
	{
		if (stack.getRawFluid() == Fluids.EMPTY)
		{
			return FluidStack.EMPTY;
		}
		else
		{
			FluidStack copy = stack.copy();
			copy.setAmount(amount);
			return copy;
		}

	}

	public static Stream<FluidStack> deriveAmount(Collection<FluidStack> stacks, int amount)
	{
		return stacks.stream().map(s -> deriveAmount(s, amount));
	}

	private FluidHelper()
	{

	}

}
