package steve_gall.create_trainwrecked.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.station.StationBlockEntity;

@Mixin(value = StationBlockEntity.class, remap = false)
public interface StationBlockEntityAccessor
{
	@Accessor
	int getAssemblyLength();

	@Accessor
	int[] getBogeyLocations();

	@Accessor
	AbstractBogeyBlock<?>[] getBogeyTypes();

	@Accessor
	boolean[] getUpsideDownBogeys();

	@Accessor
	int getBogeyCount();

	@Invoker
	void invokeException(AssemblyException exception, int carriage);

	@Invoker
	void invokeClearException();
}
