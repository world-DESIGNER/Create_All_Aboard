package steve_gall.create_trainwrecked.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.SimpleRecipeSerializer;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Keys.RECIPE_SERIALIZERS, CreateTrainwrecked.MOD_ID);
	public static final RegistryObject<SimpleRecipeSerializer<TrainEngineTypeRecipe, TrainEngineTypeRecipe.Builder<?>>> TRAIN_ENGINE_TYPE = RECIPE_SERIALIZERS.register("train_engine_type", () -> new SimpleRecipeSerializer<TrainEngineTypeRecipe, TrainEngineTypeRecipe.Builder<?>>(TrainEngineTypeRecipe.Builder::new));
	public static final RegistryObject<SimpleRecipeSerializer<TrainEngineCoolantRecipe, TrainEngineCoolantRecipe.Builder<?>>> TRAIN_ENGINE_COOLANT = RECIPE_SERIALIZERS.register("train_engine_coolant", () -> new SimpleRecipeSerializer<TrainEngineCoolantRecipe, TrainEngineCoolantRecipe.Builder<?>>(TrainEngineCoolantRecipe.Builder::new));

	private ModRecipeSerializers()
	{

	}

}
