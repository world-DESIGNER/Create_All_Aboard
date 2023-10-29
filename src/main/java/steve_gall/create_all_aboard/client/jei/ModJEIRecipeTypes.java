package steve_gall.create_all_aboard.client.jei;

import mezz.jei.api.recipe.RecipeType;
import steve_gall.create_all_aboard.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_all_aboard.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_all_aboard.common.crafting.TrainHeatSourceRecipe;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;

public class ModJEIRecipeTypes
{
	public static final RecipeType<TrainEngineTypeRecipe> TRAIN_ENGINE_TYPE = new RecipeType<>(ModRecipeTypes.TRAIN_ENGINE_TYPE.getId(), TrainEngineTypeRecipe.class);
	public static final RecipeType<TrainEngineCoolantRecipe> TRAIN_ENGINE_COOLANT = new RecipeType<>(ModRecipeTypes.TRAIN_ENGINE_COOLANT.getId(), TrainEngineCoolantRecipe.class);
	public static final RecipeType<TrainHeatSourceRecipe> TRAIN_HEAT_SOURCE = new RecipeType<>(ModRecipeTypes.TRAIN_HEAT_SOURCE.getId(), TrainHeatSourceRecipe.class);
}
