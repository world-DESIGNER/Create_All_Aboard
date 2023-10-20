package steve_gall.create_trainwrecked.client.mixin.train;

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
	@Accessor
	StationBlockEntity getBlockEntity();

	@Accessor
	GlobalStation getStation();

	@Invoker
	Train invokeGetImminent();

	@Invoker
	boolean invokeTrainPresent();
}
