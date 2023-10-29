package steve_gall.create_all_aboard.datagen;

import org.jetbrains.annotations.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModBlocks;

public class ModBlockTagProvider extends BlockTagsProvider
{
	public ModBlockTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(pGenerator, CreateAllAboard.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TRAIN_STEAM_ENGINE.get());
	}

}
