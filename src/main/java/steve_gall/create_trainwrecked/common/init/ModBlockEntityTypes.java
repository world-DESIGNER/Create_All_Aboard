package steve_gall.create_trainwrecked.common.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.block.entity.TrainSteamEngineBlockEntity;

public class ModBlockEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Keys.BLOCK_ENTITY_TYPES, CreateTrainwrecked.MOD_ID);
	public static final RegistryObject<BlockEntityType<TrainSteamEngineBlockEntity>> TRAIN_STEAM_ENGINE = BLOCK_ENTITY_TYPES.register("train_steam_engine", () -> BlockEntityType.Builder.of(TrainSteamEngineBlockEntity::new, ModBlocks.TRAIN_STEAM_ENGINE.get()).build(null));
}
