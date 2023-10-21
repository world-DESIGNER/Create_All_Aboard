package steve_gall.create_trainwrecked.datagen;

import org.jetbrains.annotations.Nullable;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModBlocks;

public class ModBlockTagProvider extends BlockTagsProvider
{
	public ModBlockTagProvider(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(pGenerator, CreateTrainwrecked.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TRAIN_STEAM_ENGINE.get());
	}

}
