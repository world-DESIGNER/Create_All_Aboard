package steve_gall.create_trainwrecked.common.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.RecipeTypeDeferredRegister.RecipeRegistryObject;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class ModRecipeTypes
{
	public static final RecipeTypeDeferredRegister RECIPE_TYPES = new RecipeTypeDeferredRegister(CreateTrainwrecked.MOD_ID);
	public static final RecipeRegistryObject<RecipeType<TrainEngineRecipe>> TRAIN_ENGINE = register("train_engine");

	private static <RECIPE extends Recipe<?>> RecipeRegistryObject<RecipeType<RECIPE>> register(String name)
	{
		ResourceLocation id = new ResourceLocation(RECIPE_TYPES.getModId(), name);
		return RECIPE_TYPES.register(name, () -> new SimpleRecpeType<RECIPE>(id));
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
