package steve_gall.create_trainwrecked.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.init.RecipeTypeDeferredRegister.RecipeRegistryObject;

public class ModRecipeTypes
{
	public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(CreateTrainwrecked.MOD_ID);
	public static final RecipeRegistryObject<RecipeType<TrainEngineTypeRecipe>> TRAIN_ENGINE_TYPE = register("train_engine_type");
	public static final RecipeRegistryObject<RecipeType<TrainEngineCoolantRecipe>> TRAIN_ENGINE_COOLANT = register("train_engine_coolant");

	private static <RECIPE extends Recipe<?>> RecipeRegistryObject<RecipeType<RECIPE>> register(String name)
	{
		ResourceLocation id = new ResourceLocation(RECIPE_TYPES.getModId(), name);
		return RECIPE_TYPES.register(name, () -> new SimpleRecpeType<>(id));
	}

	private static record SimpleRecpeType<RECIPE extends Recipe<?>> (ResourceLocation id) implements RecipeType<RECIPE>
	{
		@Override
		public String toString()
		{
			return this.id().toString();
		}

	}

	private ModRecipeTypes()
	{

	}

}
