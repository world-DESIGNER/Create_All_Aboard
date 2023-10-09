package steve_gall.create_trainwrecked.client.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.AbstractStationScreen;
import com.simibubi.create.content.trains.station.StationBlockEntity;

@Mixin(value = AbstractStationScreen.class, remap = false)
public interface AbstractStationScreenAccessor
{
	@Accessor
	StationBlockEntity getBlockEntity();

	@Invoker
	Train invokeGetImminent();

	@Invoker
	boolean invokeTrainPresent();

}
