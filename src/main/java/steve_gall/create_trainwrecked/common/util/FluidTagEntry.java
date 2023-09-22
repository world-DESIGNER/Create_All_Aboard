package steve_gall.create_trainwrecked.common.util;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.tags.TagEntry;
import net.minecraft.world.level.material.Fluid;

public class FluidTagEntry extends RegistryTagEntry<Fluid, FluidIngredient>
{
	public static final FluidTagEntryType TYPE = new FluidTagEntryType();

	public FluidTagEntry(TagEntry tagEntry)
	{
		super(tagEntry);
	}

	@Override
	public RegistryTagEntryType<Fluid, FluidIngredient, FluidTagEntry> getType()
	{
		return TYPE;
	}

}
