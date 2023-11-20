package steve_gall.create_all_aboard.client.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.AbstractStationScreen;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;

@Mixin(value = AbstractStationScreen.class)
public interface AbstractStationScreenAcessor
{
	@Accessor(remap = false)
	StationBlockEntity getBlockEntity();

	@Accessor(remap = false)
	GlobalStation getStation();

	@Invoker(remap = false)
	Train invokeGetImminent();

	@Invoker(remap = false)
	boolean invokeTrainPresent();
}
