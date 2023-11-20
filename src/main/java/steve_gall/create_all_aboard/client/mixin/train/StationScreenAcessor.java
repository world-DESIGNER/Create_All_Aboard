package steve_gall.create_all_aboard.client.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.trains.station.StationScreen;

@Mixin(value = StationScreen.class)
public interface StationScreenAcessor extends AbstractStationScreenAcessor
{
	@Invoker(remap = false)
	void invokeUpdateAssemblyTooltip(String key);
}
