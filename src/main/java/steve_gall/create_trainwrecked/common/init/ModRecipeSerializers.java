package steve_gall.create_trainwrecked.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainHeatSourceRecipe;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Keys.RECIPE_SERIALIZERS, CreateTrainwrecked.MOD_ID);
	public static final RegistryObject<TrainEngineTypeRecipe.Serializer> TRAIN_ENGINE_TYPE = RECIPE_SERIALIZERS.register("train_engine_type", TrainEngineTypeRecipe.Serializer::new);
	public static final RegistryObject<TrainEngineCoolantRecipe.Serializer> TRAIN_ENGINE_COOLANT = RECIPE_SERIALIZERS.register("train_engine_coolant", TrainEngineCoolantRecipe.Serializer::new);
	public static final RegistryObject<TrainHeatSourceRecipe.Serializer> TRAIN_HEAT_SOURCE = RECIPE_SERIALIZERS.register("train_heat_source", TrainHeatSourceRecipe.Serializer::new);

	private ModRecipeSerializers()
	{

	}

}
