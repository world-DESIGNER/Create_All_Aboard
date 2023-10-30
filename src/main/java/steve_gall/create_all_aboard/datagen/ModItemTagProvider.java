package steve_gall.create_all_aboard.datagen;

import java.util.concurrent.CompletableFuture;

import com.jesz.createdieselgenerators.blocks.BlockRegistry;
import com.simibubi.create.AllBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModItems;
import steve_gall.create_all_aboard.common.init.ModTags;

public class ModItemTagProvider extends ItemTagsProvider
{
	public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTagsProvider, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper)
	{
		super(pOutput, pLookupProvider, pBlockTagsProvider, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		IntrinsicTagAppender<Item> engines = this.tag(ModTags.Items.ENGINES);
		engines.addTag(ModTags.Items.ENGINES_STEAM);
		engines.addTag(ModTags.Items.ENGINES_DIESEL);

		IntrinsicTagAppender<Item> engines_steam = this.tag(ModTags.Items.ENGINES_STEAM);
		engines_steam.addOptional(AllBlocks.STEAM_ENGINE.getId());
		engines_steam.addOptional(ModItems.TRAIN_STEAM_ENGINE.getId());

		IntrinsicTagAppender<Item> engines_diesel = this.tag(ModTags.Items.ENGINES_DIESEL);
		engines_diesel.addOptional(new ResourceLocation("createindustry", "diesel_engine"));
		engines_diesel.addOptional(BlockRegistry.DIESEL_ENGINE.getId());
		engines_diesel.addOptional(BlockRegistry.MODULAR_DIESEL_ENGINE.getId());
	}

}
