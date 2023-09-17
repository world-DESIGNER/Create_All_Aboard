package steve_gall.create_trainwrecked.common.recipe;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class FluidTagEntryType implements RegistryTagEntryType<Fluid, FluidIngredient, FluidTagEntry>
{
	@Override
	public Fluid getEmptyValue()
	{
		return Fluids.EMPTY;
	}

	@Override
	public FluidTagEntry of(WrappedTagEntry tagEntry)
	{
		return new FluidTagEntry(tagEntry);
	}

	@Override
	public IForgeRegistry<Fluid> getRegistry()
	{
		return ForgeRegistries.FLUIDS;
	}

	@Override
	public FluidIngredient toIngredient(TagKey<Fluid> tagKey)
	{
		return FluidIngredient.fromTag(tagKey, 1);
	}

	@Override
	public FluidIngredient toIngredient(Fluid value)
	{
		return FluidIngredient.fromFluid(value, 1);
	}

}
