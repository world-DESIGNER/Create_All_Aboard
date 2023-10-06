package steve_gall.create_trainwrecked.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.trains.station.StationScreen;

@Mixin(value = StationScreen.class, remap = false)
public interface StationScreenAccessor extends AbstractStationScreenAccessor
{
	@Invoker
	void invokeUpdateAssemblyTooltip(String key);
}
