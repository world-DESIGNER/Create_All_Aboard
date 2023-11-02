package steve_gall.create_all_aboard.common.content.train;

import java.util.List;

import steve_gall.create_all_aboard.common.fluid.FluidTankData;

public interface CarriageSyncDataExtension
{
	List<FluidTankData> getSyncedFluids();

	TrainPartsSyncData<Engine> getEngineData();

	TrainPartsSyncData<HeatSource> getHeatSourceData();

	double getTrainSpeed();
}
