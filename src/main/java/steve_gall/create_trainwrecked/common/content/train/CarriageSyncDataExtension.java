package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import steve_gall.create_trainwrecked.common.fluid.FluidTankData;

public interface CarriageSyncDataExtension
{
	List<FluidTankData> getSyncedFluids();

	List<CompoundTag> getEngineCaches();

	List<CompoundTag> getHeatSourceCaches();

	double getTrainSpeed();
}
