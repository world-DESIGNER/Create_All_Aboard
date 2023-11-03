package steve_gall.create_all_aboard.datagen;

import com.drmangotea.createindustry.blocks.CIBlocks;
import com.jesz.createdieselgenerators.blocks.BlockRegistry;
import com.simibubi.create.AllBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModBlocks;
import steve_gall.create_all_aboard.common.init.ModTags;

public class ModItemTagProvider extends ItemTagsProvider
{
	public ModItemTagProvider(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper)
	{
		super(pGenerator, pBlockTagsProvider, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		TagAppender<Item> engines = this.tag(ModTags.Items.ENGINES);
		engines.addTag(ModTags.Items.ENGINES_STEAM);
		engines.addTag(ModTags.Items.ENGINES_DIESEL);

		TagAppender<Item> engines_steam = this.tag(ModTags.Items.ENGINES_STEAM);
		engines_steam.addOptional(AllBlocks.STEAM_ENGINE.getId());
		engines_steam.addOptional(ModBlocks.TRAIN_STEAM_ENGINE.getId());

		TagAppender<Item> engines_diesel = this.tag(ModTags.Items.ENGINES_DIESEL);
		engines_diesel.addOptional(CIBlocks.DIESEL_ENGINE.getId());
		engines_diesel.addOptional(BlockRegistry.DIESEL_ENGINE.getId());
		engines_diesel.addOptional(BlockRegistry.MODULAR_DIESEL_ENGINE.getId());
	}

}
