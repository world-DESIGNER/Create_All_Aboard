package steve_gall.create_all_aboard.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModTags;

public class ModFluidTagProvider extends FluidTagsProvider
{
	public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		IntrinsicTagAppender<Fluid> fuels = this.tag(ModTags.Fluids.FUELS);
		fuels.addTag(ModTags.Fluids.FUELS_STEAM);
		fuels.addTag(ModTags.Fluids.FUELS_DIESEL);

		IntrinsicTagAppender<Fluid> fuels_steam = this.tag(ModTags.Fluids.FUELS_STEAM);
		fuels_steam.add(Fluids.WATER);

		IntrinsicTagAppender<Fluid> fuels_diesel = this.tag(ModTags.Fluids.FUELS_DIESEL);
		fuels_diesel.addTag(ModTags.Fluids.DIESEL);
		fuels_diesel.addTag(ModTags.Fluids.BIODIESEL);

		this.tag(ModTags.Fluids.DIESEL);
		this.tag(ModTags.Fluids.BIODIESEL);
	}

}
