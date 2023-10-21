package steve_gall.create_trainwrecked.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider
{
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, CreateTrainwrecked.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		this.horizontalFaceBlock(ModBlocks.TRAIN_STEAM_ENGINE.get(), s -> this.models().getExistingFile(CreateTrainwrecked.asResource("block/train_steam_engine/block")));
	}

}
