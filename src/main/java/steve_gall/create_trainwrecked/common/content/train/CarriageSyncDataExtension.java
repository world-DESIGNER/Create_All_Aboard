package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import steve_gall.create_trainwrecked.common.fluid.FluidTankData;

public interface CarriageSyncDataExtension
{
	List<FluidTankData> getSyncedFluids();

	List<Engine> getEngineCaches();

	List<HeatSource> getHeatSourceCaches();

	double getTrainSpeed();
}
