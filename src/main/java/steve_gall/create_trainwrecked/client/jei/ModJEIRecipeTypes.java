package steve_gall.create_trainwrecked.client.jei;

import mezz.jei.api.recipe.RecipeType;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;

public class ModJEIRecipeTypes
{
	public static final RecipeType<TrainEngineTypeRecipe> TRAIN_ENGINE_TYPE = new RecipeType<>(ModRecipeTypes.TRAIN_ENGINE_TYPE.getId(), TrainEngineTypeRecipe.class);
	public static final RecipeType<TrainEngineCoolantRecipe> TRAIN_ENGINE_COOLANT = new RecipeType<>(ModRecipeTypes.TRAIN_ENGINE_COOLANT.getId(), TrainEngineCoolantRecipe.class);
}
