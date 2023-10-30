package steve_gall.create_all_aboard.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModBlocks;

public class ModBlockTagProvider extends BlockTagsProvider
{
	public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider)
	{
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TRAIN_STEAM_ENGINE.get());

	}

}
