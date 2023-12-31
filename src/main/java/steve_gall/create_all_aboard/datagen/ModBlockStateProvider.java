package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.init.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider
{
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, CreateAllAboard.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		this.horizontalFaceBlock(ModBlocks.TRAIN_STEAM_ENGINE.get(), s -> this.models().getExistingFile(CreateAllAboard.asResource("block/train_steam_engine/block")));
	}

}
