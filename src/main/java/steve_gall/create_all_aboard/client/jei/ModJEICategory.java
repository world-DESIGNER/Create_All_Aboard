package steve_gall.create_all_aboard.client.jei;

import java.util.List;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class ModJEICategory<RECIPE> implements IRecipeCategory<RECIPE>
{
	private final ModJEI plugin;
	private final RecipeType<RECIPE> type;
	private final IDrawable background;
	private final Component title;

	public ModJEICategory(ModJEI plugin, RecipeType<RECIPE> type, IDrawable background, String titleKey)
	{
		this.plugin = plugin;
		this.type = type;
		this.background = background;
		this.title = Component.translatable(titleKey);
	}

	public abstract List<RECIPE> getRecipes(RecipeManager recipeManager);

	public final void addRecipeCatalyst(IRecipeCatalystRegistration registration)
	{
		this.addRecipeCatalyst(new RecipeCatalystConsumer()
		{
			@Override
			public IIngredientManager getIngredientManager()
			{
				return registration.getIngredientManager();
			}

			@Override
			public void consume(ItemStack ingredient)
			{
				registration.addRecipeCatalyst(ingredient, getRecipeType());
			}

			@Override
			public <T> void consume(IIngredientType<T> type, T ingredient)
			{
				registration.addRecipeCatalyst(type, ingredient, getRecipeType());
			}

		});
	}

	protected void addRecipeCatalyst(RecipeCatalystConsumer consumer)
	{

	}

	public ModJEI getPlugin()
	{
		return this.plugin;
	}

	@Override
	public RecipeType<RECIPE> getRecipeType()
	{
		return this.type;
	}

	@Override
	public IDrawable getIcon()
	{
		return null;
	}

	@Override
	public IDrawable getBackground()
	{
		return this.background;
	}

	@Override
	public Component getTitle()
	{
		return this.title;
	}

	public interface RecipeCatalystConsumer
	{
		IIngredientManager getIngredientManager();

		void consume(ItemStack ingredient);

		<T> void consume(IIngredientType<T> type, T ingredient);
	}

}
