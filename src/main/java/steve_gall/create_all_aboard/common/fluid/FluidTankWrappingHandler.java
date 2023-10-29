package steve_gall.create_all_aboard.common.fluid;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidTankWrappingHandler implements IFluidHandler
{
	private List<FluidTankData> list;

	public FluidTankWrappingHandler(List<FluidTankData> list)
	{
		this.list = list;
	}

	@Override
	public int getTanks()
	{
		return this.list.size();
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank)
	{
		return this.list.get(tank).stack();
	}

	@Override
	public int getTankCapacity(int tank)
	{
		return this.list.get(tank).capacity();
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack)
	{
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		return 0;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action)
	{
		if (action.simulate() && !resource.isEmpty())
		{
			int toDrain = resource.getAmount();
			int drained = 0;

			for (FluidTankData tank : this.list)
			{
				FluidStack stack = tank.stack();

				if (stack.isFluidEqual(resource))
				{
					drained += Math.min(stack.getAmount(), toDrain);
					toDrain -= drained;

					if (toDrain <= 0)
					{
						break;
					}

				}

			}

			return FluidHelper.deriveAmount(resource, drained);
		}

		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action)
	{
		if (action.simulate())
		{
			for (FluidTankData tank : this.list)
			{
				FluidStack stack = tank.stack();

				if (!stack.isEmpty())
				{
					return this.drain(FluidHelper.deriveAmount(stack, maxDrain), action);
				}

			}

		}

		return FluidStack.EMPTY;
	}

}
