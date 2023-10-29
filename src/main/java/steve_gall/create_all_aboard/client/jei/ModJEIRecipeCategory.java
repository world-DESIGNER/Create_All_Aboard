package steve_gall.create_all_aboard.client.jei;

import java.util.List;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class ModJEIRecipeCategory<RECIPE extends Recipe<?>> extends ModJEICategory<RECIPE>
{
	public ModJEIRecipeCategory(ModJEI plugin, RecipeType<RECIPE> type, IDrawable background, String titleKey)
	{
		super(plugin, type, background, titleKey);
	}

	public abstract net.minecraft.world.item.crafting.RecipeType<? extends RECIPE> getCraftingRecipeType();

	@Override
	@SuppressWarnings("unchecked")
	public List<RECIPE> getRecipes(RecipeManager recipeManager)
	{
		net.minecraft.world.item.crafting.RecipeType<Recipe<Container>> recipeType = (net.minecraft.world.item.crafting.RecipeType<Recipe<Container>>) this.getCraftingRecipeType();
		List<RECIPE> allRecipesFor = (List<RECIPE>) recipeManager.getAllRecipesFor(recipeType);
		return allRecipesFor.stream().filter(this::test).toList();
	}

	public boolean test(RECIPE recipe)
	{
		return true;
	}

}
