package steve_gall.create_trainwrecked.client.jei;

import java.util.Collection;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class ModJEICategory<RECIPE> implements IRecipeCategory<RECIPE>
{
	private final IJeiHelpers helpers;
	private final ResourceLocation id;
	private final Class<? extends RECIPE> recipeClass;
	private final IDrawable background;
	private final Component title;

	public ModJEICategory(IJeiHelpers helpers, ResourceLocation id, Class<? extends RECIPE> recipeClass, IDrawable background, String titleKey)
	{
		this.helpers = helpers;
		this.id = id;
		this.recipeClass = recipeClass;
		this.background = background;
		this.title = new TranslatableComponent(titleKey);
	}

	public abstract Collection<RECIPE> getRecipes(RecipeManager recipeManager);

	@SuppressWarnings("removal")
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
				registration.addRecipeCatalyst(ingredient, getUid());
			}

			@Override
			public <T> void consume(IIngredientType<T> type, T ingredient)
			{
				registration.addRecipeCatalyst(type, ingredient, getUid());
			}

		});
	}

	protected void addRecipeCatalyst(RecipeCatalystConsumer consumer)
	{

	}

	public IJeiHelpers getHelpers()
	{
		return this.helpers;
	}

	@Override
	public IDrawable getIcon()
	{
		return null;
	}

	@Override
	public ResourceLocation getUid()
	{
		return this.id;
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

	@Override
	public Class<? extends RECIPE> getRecipeClass()
	{
		return this.recipeClass;
	}

	public interface RecipeCatalystConsumer
	{
		IIngredientManager getIngredientManager();

		void consume(ItemStack ingredient);

		<T> void consume(IIngredientType<T> type, T ingredient);
	}

}
