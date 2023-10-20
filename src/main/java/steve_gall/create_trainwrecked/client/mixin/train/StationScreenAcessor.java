package steve_gall.create_trainwrecked.client.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.trains.station.StationScreen;

@Mixin(value = StationScreen.class)
public interface StationScreenAcessor extends AbstractStationScreenAcessor
{
	@Invoker
	void invokeUpdateAssemblyTooltip(String key);
}
