package steve_gall.create_trainwrecked.client.jei;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;
import steve_gall.create_trainwrecked.common.util.NumberHelper;

public class TrainEngineTypeCategory extends ModJEIRecipeCategory<TrainEngineTypeRecipe>
{
	public static final RecipeType<TrainEngineTypeRecipe> RECIPE_TYPE = ModJEIRecipeTypes.TRAIN_ENGINE_TYPE;
	public static final ResourceLocation TEXTURE_BACKGROUND = ModJEI.texture(RECIPE_TYPE.getUid());
	public static final String TEXT_TITLE = ModJEI.translationKey(RECIPE_TYPE, "title");
	public static final String TEXT_MAX_CARRIAGES = ModJEI.translationKey(RECIPE_TYPE, "max_carriages");
	public static final String TEXT_MAX_BLOCKS_PER_CARRIAGE = ModJEI.translationKey(RECIPE_TYPE, "max_blocks_per_carriage");
	public static final String TEXT_MAX_SPEED = ModJEI.translationKey(RECIPE_TYPE, "max_speed");
	public static final String TEXT_ACCELERATION = ModJEI.translationKey(RECIPE_TYPE, "acceleration");
	public static final String TEXT_MAX_FUEL_USAGE = ModJEI.translationKey(RECIPE_TYPE, "max_fuel_usage");
	public static final String TEXT_HEAT_LEVEL_FOR_MAX_SPEED = ModJEI.translationKey(RECIPE_TYPE, "need_heat_level");
	public static final String TEXT_HEAT_CAPACITY = ModJEI.translationKey(RECIPE_TYPE, "heat_capacity");
	public static final String TEXT_HEAT_PER_FUEL = ModJEI.translationKey(RECIPE_TYPE, "heat_per_fuel");
	public static final String TEXT_AIR_COOLING_RATE = ModJEI.translationKey(RECIPE_TYPE, "air_cooling_rate");
	public static final String TEXT_HEAT_DURABILITY = ModJEI.translationKey(RECIPE_TYPE, "heat_durability");

	public TrainEngineTypeCategory(ModJEI plugin)
	{
		super(plugin, RECIPE_TYPE, plugin.getJeiHelpers().getGuiHelper().createDrawable(TEXTURE_BACKGROUND, 0, 0, 178, 120), TEXT_TITLE);
	}

	@Override
	public net.minecraft.world.item.crafting.RecipeType<? extends TrainEngineTypeRecipe> getCraftingRecipeType()
	{
		return ModRecipeTypes.TRAIN_ENGINE_TYPE.get();
	}

	@Override
	protected void addRecipeCatalyst(RecipeCatalystConsumer consumer)
	{
		super.addRecipeCatalyst(consumer);

		consumer.consume(new ItemStack(AllBlocks.TRAIN_CONTROLS.get()));
	}

	@Override
	public void draw(TrainEngineTypeRecipe recipe, IRecipeSlotsView slotsView, PoseStack stack, double mouseX, double mouseY)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		float maxSpeed = recipe.getMaxSpeed();
		int heatLevel = 0;

		if (recipe.isLimitableByHeat())
		{
			List<? extends Number> speedLimits = CreateTrainwreckedConfig.COMMON.heatSourceSpeedLimits.get();

			for (int i = 0; i < speedLimits.size(); i++)
			{
				if (speedLimits.get(i).floatValue() >= maxSpeed)
				{
					heatLevel = i + 1;
				}

			}

		}

		double maxFuelUsage = recipe.getFuelUsage(maxSpeed, heatLevel);

		int textX = 2;
		int textY = 22;
		int textColor = 0x404040;
		font.draw(stack, Component.translatable(TEXT_MAX_CARRIAGES, NumberHelper.format(recipe.getMaxCarriageCount(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, Component.translatable(TEXT_MAX_BLOCKS_PER_CARRIAGE, NumberHelper.format(recipe.getMaxBlockCountPerCarriage(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, Component.translatable(TEXT_MAX_SPEED, NumberHelper.format(recipe.getMaxSpeed(), 2) + " m/s"), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, Component.translatable(TEXT_ACCELERATION, NumberHelper.format(recipe.getAcceleration(), 2) + " m/s²"), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, Component.translatable(TEXT_MAX_FUEL_USAGE, NumberHelper.format(maxFuelUsage, 2) + " mB/s"), textX, textY, textColor);
		textY += font.lineHeight;

		if (recipe.isLimitableByHeat())
		{
			font.draw(stack, Component.translatable(TEXT_HEAT_LEVEL_FOR_MAX_SPEED, NumberHelper.format(heatLevel)), textX, textY, textColor);
			textY += font.lineHeight;
		}

		if (recipe.getHeatPerFuel() > 0 && recipe.hasHeatCapacity())
		{
			textY += font.lineHeight;
			font.draw(stack, Component.translatable(TEXT_HEAT_CAPACITY, NumberHelper.format(recipe.getHeatCapacity()) + " J"), textX, textY, textColor);
			textY += font.lineHeight;

			font.draw(stack, Component.translatable(TEXT_HEAT_PER_FUEL, NumberHelper.format(recipe.getHeatPerFuel(), 2) + " J/mB"), textX, textY, textColor);
			textY += font.lineHeight;

			font.draw(stack, Component.translatable(TEXT_AIR_COOLING_RATE, NumberHelper.format(recipe.getAirCoolingRate(), 2) + " J/s"), textX, textY, textColor);
			textY += font.lineHeight;

			double heatDuration = recipe.getHeatDuration(maxFuelUsage);
			font.draw(stack, Component.translatable(TEXT_HEAT_DURABILITY, NumberHelper.format(heatDuration, 2) + " s"), textX, textY, textColor);
			textY += font.lineHeight;
		}

	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder layout, TrainEngineTypeRecipe recipe, IFocusGroup focus)
	{
		layout.addSlot(RecipeIngredientRole.INPUT, 2, 2).addItemStacks(ItemTagEntry.TYPE.getIngredientMatchingStacks(recipe.getBlocks()).toList());

		List<FluidStack> fluids = FluidHelper.deriveAmount(FluidTagEntry.TYPE.getIngredientMatchingStacks(recipe.getFuels()).toList(), FluidType.BUCKET_VOLUME).toList();
		layout.addSlot(RecipeIngredientRole.INPUT, 26, 2).addIngredients(ForgeTypes.FLUID_STACK, fluids);
	}

}
