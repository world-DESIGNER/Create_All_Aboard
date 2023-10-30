package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModTags;

public class ModFluidTagProvider extends FluidTagsProvider
{
	public ModFluidTagProvider(DataGenerator pGenerator, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper)
	{
		super(pGenerator, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		TagAppender<Fluid> fuels = this.tag(ModTags.Fluids.FUELS);
		fuels.addTag(ModTags.Fluids.FUELS_STEAM);
		fuels.addTag(ModTags.Fluids.FUELS_DIESEL);

		TagAppender<Fluid> fuels_steam = this.tag(ModTags.Fluids.FUELS_STEAM);
		fuels_steam.add(Fluids.WATER);

		TagAppender<Fluid> fuels_diesel = this.tag(ModTags.Fluids.FUELS_DIESEL);
		fuels_diesel.addTag(ModTags.Fluids.DIESEL);
		fuels_diesel.addTag(ModTags.Fluids.BIODIESEL);

		this.tag(ModTags.Fluids.DIESEL);
		this.tag(ModTags.Fluids.BIODIESEL);
	}

}
