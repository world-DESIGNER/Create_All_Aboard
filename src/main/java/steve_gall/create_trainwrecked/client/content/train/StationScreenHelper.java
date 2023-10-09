package steve_gall.create_trainwrecked.client.content.train;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.StationScreen;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;

import steve_gall.create_trainwrecked.client.mixin.train.StationScreenAccessor;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;
import steve_gall.create_trainwrecked.common.mixin.train.StationBlockEntityAccessor;

public class StationScreenHelper
{
	public static final String STATION_ENGINE_HAS_HEAT = "has_heat";

	public static void tickTail(StationScreen screen)
	{
		StationScreenAccessor screenAccessor = (StationScreenAccessor) screen;
		StationBlockEntity blockEntity = screenAccessor.getBlockEntity();
		StationBlockEntityAccessor stationAccessor = (StationBlockEntityAccessor) blockEntity;
		TrackTargetingBehaviour<GlobalStation> edgePoint = blockEntity.edgePoint;

		if (!edgePoint.isOnCurve() && edgePoint.isOrthogonal() && screenAccessor.invokeTrainPresent())
		{
			GlobalStation station = blockEntity.getStation();

			if (station != null && !stationAccessor.isTrainCanDisassemble())
			{
				Train train = screenAccessor.invokeGetImminent();

				if (train != null && TrainHelper.anyEngineHasHeat(train))
				{
					screenAccessor.invokeUpdateAssemblyTooltip(STATION_ENGINE_HAS_HEAT);
				}

			}

		}

	}

	private StationScreenHelper()
	{

	}

}
