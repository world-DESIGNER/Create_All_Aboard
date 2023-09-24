package steve_gall.create_trainwrecked.common.content.contraption;

import java.util.Collection;

import net.minecraftforge.fluids.capability.IFluidHandler;
import steve_gall.create_trainwrecked.common.fluid.FluidTankData;

public interface MountedStorageManagerExtension
{
	void setSyncedFluids(Collection<FluidTankData> fluids);

	IFluidHandler getSyncedFluids();
}
