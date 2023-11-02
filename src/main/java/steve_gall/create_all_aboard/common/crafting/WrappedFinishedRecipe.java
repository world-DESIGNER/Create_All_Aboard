package steve_gall.create_all_aboard.common.crafting;

import net.minecraft.data.recipes.FinishedRecipe;

public class WrappedFinishedRecipe<RECIPE extends SerializableRecipe<?>> extends FinishedRecipeWrapper
{
	private final RECIPE recipe;

	public WrappedFinishedRecipe(RECIPE recipe, FinishedRecipe finishedRecipe)
	{
		super(finishedRecipe);
		this.recipe = recipe;
	}

	public RECIPE getRecipe()
	{
		return this.recipe;
	}

}
