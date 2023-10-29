package steve_gall.create_all_aboard.common.content.contraption;

import java.util.Collection;

import net.minecraftforge.fluids.capability.IFluidHandler;
import steve_gall.create_all_aboard.common.fluid.FluidTankData;

public interface MountedStorageManagerExtension
{
	void setSyncedFluids(Collection<FluidTankData> fluids);

	IFluidHandler getSyncedFluids();
}
