package steve_gall.create_all_aboard.common.content.train;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import steve_gall.create_all_aboard.common.fluid.FluidTankData;

public interface CarriageSyncDataExtension
{
	List<FluidTankData> getSyncedFluids();

	List<CompoundTag> getEngineCaches();

	List<CompoundTag> getHeatSourceCaches();

	double getTrainSpeed();
}
