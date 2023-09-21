package steve_gall.create_trainwrecked.client.jei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;

@JeiPlugin
public class ModJEI implements IModPlugin
{
	private final List<ModJEICategory<?>> categories = new ArrayList<>();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		this.categories.clear();
		this.categories.add(new TrainEngineTypeCategory(jeiHelpers));
		this.categories.add(new TrainEngineCoolantCategory(jeiHelpers));

		for (ModJEICategory<?> category : this.categories)
		{
			registration.addRecipeCategories(category);
		}

	}

	@SuppressWarnings("removal")
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		Minecraft minecraft = Minecraft.getInstance();
		RecipeManager recipeManager = minecraft.player.level.getRecipeManager();

		for (ModJEICategory<?> category : this.categories)
		{
			Collection<?> recipes = category.getRecipes(recipeManager);
			registration.addRecipes(recipes, category.getUid());
		}

	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		for (ModJEICategory<?> category : this.categories)
		{
			category.addRecipeCatalyst(registration);
		}

	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return CreateTrainwrecked.asResource("jei_plugin");
	}

	public static ResourceLocation texture(RecipeType<?> recipe, String path)
	{
		ResourceLocation id = recipe.getUid();
		return texture(id.getNamespace(), new StringBuilder().append(id.getPath().replace('.', '/')).append(".").append(path));
	}

	public static ResourceLocation texture(ResourceLocation id)
	{
		return texture(id.getNamespace(), id.getPath());
	}

	public static ResourceLocation texture(CharSequence path)
	{
		return texture(CreateTrainwrecked.MOD_ID, path);
	}

	public static ResourceLocation texture(String namespace, CharSequence path)
	{
		StringBuilder builder = new StringBuilder().append("textures/jei/").append(path).append(".png");
		return new ResourceLocation(namespace, builder.toString());
	}

	public static String translationKey(ResourceLocation type, CharSequence path)
	{
		return translationKey(type.getNamespace(), new StringBuilder().append(type.getPath()).append(".").append(path));
	}

	public static String translationKey(ResourceLocation id)
	{
		return translationKey(id.getNamespace(), id.getPath());
	}

	public static String translationKey(CharSequence path)
	{
		return translationKey(CreateTrainwrecked.MOD_ID, path);
	}

	public static String translationKey(String namespace, CharSequence path)
	{
		return CreateTrainwrecked.translationKey("text", namespace, new StringBuilder("jei.").append(path).toString());
	}

}
