package steve_gall.create_all_aboard.client.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.crafting.HeatStage;

@JeiPlugin
public class ModJEI implements IModPlugin
{
	private final List<ModJEICategory<?>> categories = new ArrayList<>();
	private final Map<RecipeType<?>, ModJEICategory<?>> categoryMap = new HashMap<>();
	private final List<ItemStack> burnableItems = new ArrayList<>();
	private final List<ItemStack> blazeBunerFuels = new ArrayList<>();

	private IJeiHelpers jeiHelpers;
	private TrainEngineTypeCategory trainEngineTypeCategory;
	private TrainEngineCoolantCategory trainEngineCoolantCategory;
	private TrainHeatSourceCategory trainHeatSourceCategory;

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		this.jeiHelpers = jeiHelpers;

		this.categoryMap.clear();
		this.categories.clear();
		this.categories.add(this.trainEngineTypeCategory = new TrainEngineTypeCategory(this));
		this.categories.add(this.trainEngineCoolantCategory = new TrainEngineCoolantCategory(this));
		this.categories.add(this.trainHeatSourceCategory = new TrainHeatSourceCategory(this));

		for (ModJEICategory<?> category : this.categories)
		{
			this.categoryMap.put(category.getRecipeType(), category);
			registration.addRecipeCategories(category);
		}

	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		Minecraft minecraft = Minecraft.getInstance();
		RecipeManager recipeManager = minecraft.player.level.getRecipeManager();

		this.cacheBurnTimes();
		this.cacheBlazeBurnerFuels();

		for (ModJEICategory<?> category : this.categories)
		{
			this.registerRecipe(registration, category, recipeManager);
		}

	}

	private void cacheBurnTimes()
	{
		this.burnableItems.clear();

		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			ItemStack stack = new ItemStack(item);
			int burnTime = ForgeHooks.getBurnTime(stack, null);

			if (burnTime > 0 && HeatStage.getBlazeBurnerFuelBurnTime(stack) == 0)
			{
				this.burnableItems.add(stack);
			}

		}

	}

	private void cacheBlazeBurnerFuels()
	{
		this.blazeBunerFuels.clear();

		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			ItemStack stack = new ItemStack(item);

			if (HeatStage.getBlazeBurnerFuelBurnTime(stack) > 0)
			{
				this.blazeBunerFuels.add(stack);
			}

		}

	}

	public <RECIPE> void registerRecipe(IRecipeRegistration registration, ModJEICategory<RECIPE> category, RecipeManager recipeManager)
	{
		RecipeType<RECIPE> recipeType = category.getRecipeType();
		List<RECIPE> recipes = category.getRecipes(recipeManager);
		registration.addRecipes(recipeType, recipes);
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
		return CreateAllAboard.asResource("jei_plugin");
	}

	public IJeiHelpers getJeiHelpers()
	{
		return this.jeiHelpers;
	}

	public List<ModJEICategory<?>> getCategories()
	{
		return Collections.unmodifiableList(this.categories);
	}

	public Map<RecipeType<?>, ModJEICategory<?>> getCategoryMap()
	{
		return Collections.unmodifiableMap(this.categoryMap);
	}

	public List<ItemStack> getBurnableItems()
	{
		return Collections.unmodifiableList(this.burnableItems);
	}

	public List<ItemStack> getBlazeBunerFuels()
	{
		return Collections.unmodifiableList(this.blazeBunerFuels);
	}

	public TrainEngineTypeCategory getTrainEngineTypeCategory()
	{
		return this.trainEngineTypeCategory;
	}

	public TrainEngineCoolantCategory getTrainEngineCoolantCategory()
	{
		return this.trainEngineCoolantCategory;
	}

	public TrainHeatSourceCategory getTrainHeatSourceCategory()
	{
		return this.trainHeatSourceCategory;
	}

	public static ResourceLocation texture(RecipeType<?> recipe, CharSequence path)
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
		return texture(CreateAllAboard.MOD_ID, path);
	}

	public static ResourceLocation texture(String namespace, CharSequence path)
	{
		StringBuilder builder = new StringBuilder().append("textures/jei/").append(path).append(".png");
		return new ResourceLocation(namespace, builder.toString());
	}

	public static String translationKey(RecipeType<?> type, CharSequence path)
	{
		ResourceLocation uid = type.getUid();
		return translationKey(uid.getNamespace(), new StringBuilder().append(uid.getPath()).append(".").append(path));
	}

	public static String translationKey(ResourceLocation id)
	{
		return translationKey(id.getNamespace(), id.getPath());
	}

	public static String translationKey(CharSequence path)
	{
		return translationKey(CreateAllAboard.MOD_ID, path);
	}

	public static String translationKey(String namespace, CharSequence path)
	{
		return CreateAllAboard.translationKey("text", namespace, new StringBuilder("jei.").append(path).toString());
	}

}
