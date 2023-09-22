package steve_gall.create_trainwrecked.client.jei;

import java.util.List;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class ModJEIRecipeCategory<RECIPE extends Recipe<?>> extends ModJEICategory<RECIPE>
{
	public ModJEIRecipeCategory(IJeiHelpers helpers, RecipeType<RECIPE> type, IDrawable background, String titleKey)
	{
		super(helpers, type, background, titleKey);
	}

	public abstract net.minecraft.world.item.crafting.RecipeType<? extends RECIPE> getCraftingRecipeType();

	@Override
	@SuppressWarnings("unchecked")
	public List<RECIPE> getRecipes(RecipeManager recipeManager)
	{
		net.minecraft.world.item.crafting.RecipeType<Recipe<Container>> recipeType = (net.minecraft.world.item.crafting.RecipeType<Recipe<Container>>) this.getCraftingRecipeType();
		return (List<RECIPE>) recipeManager.getAllRecipesFor(recipeType);
	}

}
