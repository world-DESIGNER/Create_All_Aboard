package steve_gall.create_trainwrecked.client.jei;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.NumberHelper;

public class TrainEngineTypeCategory extends ModJEIRecipeCategory<TrainEngineTypeRecipe>
{
	public static final ResourceLocation RECIPE_TYPE = ModJEIRecipeTypes.TRAIN_ENGINE_TYPE;
	public static final ResourceLocation TEXTURE_BACKGROUND = ModJEI.texture(RECIPE_TYPE);
	public static final String TEXT_TITLE = ModJEI.translationKey(RECIPE_TYPE, "title");
	public static final String TEXT_MAX_CARRIAGES = ModJEI.translationKey(RECIPE_TYPE, "max_carriages");
	public static final String TEXT_MAX_BLOCKS_PER_CARRIAGE = ModJEI.translationKey(RECIPE_TYPE, "max_blocks_per_carriage");
	public static final String TEXT_MAX_SPEED = ModJEI.translationKey(RECIPE_TYPE, "max_speed");
	public static final String TEXT_ACCELERATION = ModJEI.translationKey(RECIPE_TYPE, "acceleration");
	public static final String TEXT_MAX_FUEL_USAGE = ModJEI.translationKey(RECIPE_TYPE, "max_fuel_usage");
	public static final String TEXT_HEAT_CAPACITY = ModJEI.translationKey(RECIPE_TYPE, "heat_capacity");
	public static final String TEXT_HEAT_PER_FUEL = ModJEI.translationKey(RECIPE_TYPE, "heat_per_fuel");
	public static final String TEXT_AIR_COOLING_RATE = ModJEI.translationKey(RECIPE_TYPE, "air_cooling_rate");
	public static final String TEXT_HEAT_DURABILITY = ModJEI.translationKey(RECIPE_TYPE, "heat_durability");

	public TrainEngineTypeCategory(IJeiHelpers helpers)
	{
		super(helpers, RECIPE_TYPE, TrainEngineTypeRecipe.class, helpers.getGuiHelper().createDrawable(TEXTURE_BACKGROUND, 0, 0, 178, 120), TEXT_TITLE);
	}

	@Override
	public RecipeType<? extends TrainEngineTypeRecipe> getCraftingRecipeType()
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
		double maxFuelUsage = recipe.getFuelUsage(0, maxSpeed);

		int textX = 2;
		int textY = 22;
		int textColor = 0x404040;
		font.draw(stack, new TranslatableComponent(TEXT_MAX_CARRIAGES, NumberHelper.format(recipe.getMaxCarriageCount(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, new TranslatableComponent(TEXT_MAX_BLOCKS_PER_CARRIAGE, NumberHelper.format(recipe.getMaxBlockCountPerCarriage(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, new TranslatableComponent(TEXT_MAX_SPEED, NumberHelper.format(recipe.getMaxSpeed(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, new TranslatableComponent(TEXT_ACCELERATION, NumberHelper.format(recipe.getAcceleration(), 2)), textX, textY, textColor);
		textY += font.lineHeight;

		font.draw(stack, new TranslatableComponent(TEXT_MAX_FUEL_USAGE, NumberHelper.format(maxFuelUsage, 2)), textX, textY, textColor);
		textY += font.lineHeight;

		if (recipe.getHeatPerFuel() > 0 && recipe.getHeatCapacity() > 0)
		{
			textY += font.lineHeight;
			font.draw(stack, new TranslatableComponent(TEXT_HEAT_CAPACITY, NumberHelper.format(recipe.getHeatCapacity())), textX, textY, textColor);
			textY += font.lineHeight;

			font.draw(stack, new TranslatableComponent(TEXT_HEAT_PER_FUEL, NumberHelper.format(recipe.getHeatPerFuel())), textX, textY, textColor);
			textY += font.lineHeight;

			font.draw(stack, new TranslatableComponent(TEXT_AIR_COOLING_RATE, NumberHelper.format(recipe.getAirCoolingRate())), textX, textY, textColor);
			textY += font.lineHeight;

			double heatDuration = recipe.getHeatDuration(maxFuelUsage);
			font.draw(stack, new TranslatableComponent(TEXT_HEAT_DURABILITY, NumberHelper.format(heatDuration, 2)), textX, textY, textColor);
			textY += font.lineHeight;
		}

	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder layout, TrainEngineTypeRecipe recipe, IFocusGroup focus)
	{
		layout.addSlot(RecipeIngredientRole.INPUT, 2, 2).addIngredients(recipe.getBlock());

		List<FluidStack> fluids = FluidHelper.deriveAmount(recipe.getFuelType().toIngredient().getMatchingFluidStacks(), FluidAttributes.BUCKET_VOLUME).toList();
		layout.addSlot(RecipeIngredientRole.INPUT, 26, 2).addIngredients(ForgeTypes.FLUID_STACK, fluids);
	}

}
