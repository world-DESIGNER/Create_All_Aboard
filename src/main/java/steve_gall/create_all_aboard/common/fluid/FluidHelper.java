package steve_gall.create_all_aboard.common.fluid;

import java.util.Collection;
import java.util.stream.Stream;

import net.minecraftforge.fluids.FluidStack;

public class FluidHelper
{
	public static FluidStack deriveAmount(FluidStack stack, int amount)
	{
		if (stack.isEmpty())
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
