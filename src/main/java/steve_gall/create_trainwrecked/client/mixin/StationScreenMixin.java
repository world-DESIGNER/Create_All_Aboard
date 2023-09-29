package steve_gall.create_trainwrecked.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.station.AbstractStationScreen;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.station.StationScreen;

import steve_gall.create_trainwrecked.client.content.train.StationScreenHelper;

@Mixin(value = StationScreen.class)
public abstract class StationScreenMixin extends AbstractStationScreen
{
	public StationScreenMixin(StationBlockEntity be, GlobalStation station)
	{
		super(be, station);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
	private void tick(CallbackInfo ci)
	{
		StationScreenHelper.tickTail((StationScreen) (Object) this);
	}

}
