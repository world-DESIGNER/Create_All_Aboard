package steve_gall.create_all_aboard.common.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_all_aboard.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_all_aboard.common.crafting.TrainHeatSourceRecipe;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Keys.RECIPE_SERIALIZERS, CreateAllAboard.MOD_ID);
	public static final RegistryObject<TrainEngineTypeRecipe.Serializer> TRAIN_ENGINE_TYPE = RECIPE_SERIALIZERS.register("train_engine_type", TrainEngineTypeRecipe.Serializer::new);
	public static final RegistryObject<TrainEngineCoolantRecipe.Serializer> TRAIN_ENGINE_COOLANT = RECIPE_SERIALIZERS.register("train_engine_coolant", TrainEngineCoolantRecipe.Serializer::new);
	public static final RegistryObject<TrainHeatSourceRecipe.Serializer> TRAIN_HEAT_SOURCE = RECIPE_SERIALIZERS.register("train_heat_source", TrainHeatSourceRecipe.Serializer::new);

	private ModRecipeSerializers()
	{

	}

}
