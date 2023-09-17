package steve_gall.create_trainwrecked.common.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.foundation.utility.NBTHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import steve_gall.create_trainwrecked.common.content.train.CarriageExtension;
import steve_gall.create_trainwrecked.common.content.train.Engine;

@Mixin(value = Carriage.class, remap = false)
public abstract class CarriageMixin implements CarriageExtension
{
	@Inject(method = "read", at = @At(value = "TAIL"), cancellable = true)
	private static void read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions, CallbackInfoReturnable<Carriage> cir)
	{
		if (cir.getReturnValue() instanceof CarriageExtension extension)
		{
			List<Engine> engines = extension.getEngines();
			NBTHelper.iterateCompoundList(tag.getList("engines", Tag.TAG_COMPOUND), c -> engines.add(new Engine(c)));
		}

	}

	@Inject(method = "write", at = @At(value = "TAIL"), cancellable = true)
	private void write(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir)
	{
		CompoundTag tag = cir.getReturnValue();
		tag.put("engines", NBTHelper.writeCompoundList(this.getEngines(), e -> e.writeNBT()));
	}

	@Unique
	private List<Engine> engines = new ArrayList<>();

	@Override
	@Unique
	public List<Engine> getEngines()
	{
		return this.engines;
	}

}
