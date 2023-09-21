package steve_gall.create_trainwrecked.client.jei;

import java.util.Collection;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class ModJEIRecipeCategory<RECIPE extends Recipe<?>> extends ModJEICategory<RECIPE>
{
	public ModJEIRecipeCategory(IJeiHelpers helpers, ResourceLocation id, Class<? extends RECIPE> recipeClass, IDrawable background, String titleKey)
	{
		super(helpers, id, recipeClass, background, titleKey);
	}

	public abstract RecipeType<? extends RECIPE> getCraftingRecipeType();

	@Override
	@SuppressWarnings("unchecked")
	public Collection<RECIPE> getRecipes(RecipeManager recipeManager)
	{
		RecipeType<Recipe<Container>> recipeType = (RecipeType<Recipe<Container>>) this.getCraftingRecipeType();
		return (Collection<RECIPE>) recipeManager.getAllRecipesFor(recipeType);
	}

}
