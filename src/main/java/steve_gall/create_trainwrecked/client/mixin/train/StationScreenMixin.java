package steve_gall.create_trainwrecked.client.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.AbstractStationScreen;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.StationScreen;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;

import steve_gall.create_trainwrecked.client.content.train.StationScreenHelper;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;
import steve_gall.create_trainwrecked.common.mixin.train.StationBlockEntityAccessor;

@Mixin(value = StationScreen.class)
public abstract class StationScreenMixin extends AbstractStationScreen
{
	public StationScreenMixin(StationBlockEntity be, GlobalStation station)
	{
		super(be, station);
	}

	@Shadow(remap = false)
	private void updateAssemblyTooltip(String key)
	{

	}

	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		StationBlockEntity blockEntity = this.blockEntity;
		StationBlockEntityAccessor stationAccessor = (StationBlockEntityAccessor) blockEntity;
		TrackTargetingBehaviour<GlobalStation> edgePoint = blockEntity.edgePoint;

		if (!edgePoint.isOnCurve() && edgePoint.isOrthogonal() && this.trainPresent())
		{
			GlobalStation station = blockEntity.getStation();

			if (station != null && !stationAccessor.isTrainCanDisassemble())
			{
				Train train = this.getImminent();

				if (train != null && TrainHelper.anyEngineHasHeat(train))
				{
					this.updateAssemblyTooltip(StationScreenHelper.STATION_ENGINE_HAS_HEAT);
				}

			}

		}

	}

}
