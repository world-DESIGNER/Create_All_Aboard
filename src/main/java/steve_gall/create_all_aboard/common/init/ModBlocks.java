package steve_gall.create_all_aboard.common.init;

import com.simibubi.create.foundation.data.SharedProperties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.block.TrainSteamEngineBlock;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Keys.BLOCKS, CreateAllAboard.MOD_ID);
	public static final RegistryObject<TrainSteamEngineBlock> TRAIN_STEAM_ENGINE = BLOCKS.register("train_steam_engine", () -> new TrainSteamEngineBlock(BlockBehaviour.Properties.copy(SharedProperties.copperMetal())));
}
