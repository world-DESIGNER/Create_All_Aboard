package steve_gall.create_trainwrecked.client.jei;

import com.simibubi.create.AllBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;

@JeiPlugin
public class ModJEI implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		registration.addRecipeCategories(new TrainEngineCategory(jeiHelpers));
	}

	@SuppressWarnings("removal")
	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		Minecraft minecraft = Minecraft.getInstance();
		RecipeManager recipeManager = minecraft.player.level.getRecipeManager();
		registration.addRecipes(recipeManager.getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE.get()), ModJEIRecipeTypes.TRAIN_ENGINE);
	}

	@SuppressWarnings("removal")
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(AllBlocks.TRAIN_CONTROLS.get()), ModJEIRecipeTypes.TRAIN_ENGINE);
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
