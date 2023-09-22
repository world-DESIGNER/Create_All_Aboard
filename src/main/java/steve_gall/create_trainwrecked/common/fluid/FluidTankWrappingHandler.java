package steve_gall.create_trainwrecked.common.fluid;

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
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action)
	{
		return FluidStack.EMPTY;
	}

}
