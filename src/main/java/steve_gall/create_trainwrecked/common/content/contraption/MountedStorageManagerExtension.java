package steve_gall.create_trainwrecked.common.content.contraption;

import java.util.Collection;

import steve_gall.create_trainwrecked.common.fluid.FluidTankData;
import steve_gall.create_trainwrecked.common.fluid.FluidTankWrappingHandler;

public interface MountedStorageManagerExtension
{
	void setSyncedFluids(Collection<FluidTankData> fluids);

	FluidTankWrappingHandler getSyncedFluids();
}
