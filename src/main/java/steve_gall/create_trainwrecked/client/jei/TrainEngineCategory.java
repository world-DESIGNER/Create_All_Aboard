package steve_gall.create_trainwrecked.client.jei;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class TrainEngineCategory implements IRecipeCategory<TrainEngineRecipe>
{
	public static final ResourceLocation TEXTURE_BACKGROUND = ModJEI.texture(ModJEIRecipeTypes.TRAIN_ENGINE);
	public static final String TEXT_TITLE = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "title");
	public static final String TEXT_MAX_BOGEY = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "max_bogey");
	public static final String TEXT_MAX_SPEED = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "max_speed");
	public static final String TEXT_ACCELERATION = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "acceleration");
	public static final String TEXT_MAX_FUEL_USAGE = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "max_fuel_usage");
	public static final String TEXT_HEAT_DURABILITY = ModJEI.translationKey(ModJEIRecipeTypes.TRAIN_ENGINE, "heat_durability");

	private final IDrawable background;
	private final Component title;

	public TrainEngineCategory(IJeiHelpers jeiHelpers)
	{
		this.background = jeiHelpers.getGuiHelper().createDrawable(TEXTURE_BACKGROUND, 0, 0, 178, 48);
		this.title = new TranslatableComponent(TEXT_TITLE);
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
	public IDrawable getIcon()
	{
		return null;
	}

	@Override
	public ResourceLocation getUid()
	{
		return ModRecipeTypes.TRAIN_ENGINE.getId();
	}

	@Override
	public Class<? extends TrainEngineRecipe> getRecipeClass()
	{
		return TrainEngineRecipe.class;
	}

	@Override
	public void draw(TrainEngineRecipe recipe, IRecipeSlotsView slotsView, PoseStack stack, double mouseX, double mouseY)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		float maxFuelUsage = recipe.getFuelPerSpeed() * recipe.getMaxSpeed();

		int textX = 26;
		int textY = 4;
		font.draw(stack, new TranslatableComponent(TEXT_MAX_BOGEY, recipe.getMaxBogeyCount()), textX, textY, 0x000000);
		textY += font.lineHeight;
		font.draw(stack, new TranslatableComponent(TEXT_MAX_SPEED, recipe.getMaxSpeed()), textX, textY, 0x000000);
		textY += font.lineHeight;
		font.draw(stack, new TranslatableComponent(TEXT_ACCELERATION, recipe.getAcceleration()), textX, textY, 0x000000);
		textY += font.lineHeight;
		font.draw(stack, new TranslatableComponent(TEXT_MAX_FUEL_USAGE, maxFuelUsage), textX, textY, 0x000000);
		textY += font.lineHeight;
		font.draw(stack, new TranslatableComponent(TEXT_HEAT_DURABILITY, recipe.getHeatCapacity() / ((recipe.getHeatPerFuel() * maxFuelUsage) - recipe.getAirCoolingRate())), textX, textY, 0x000000);
		textY += font.lineHeight;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder layout, TrainEngineRecipe recipe, IFocusGroup focus)
	{
		layout.addSlot(RecipeIngredientRole.INPUT, 4, 4).addIngredients(recipe.getBlock());

		List<FluidStack> fluids = FluidHelper.deriveAmount(recipe.getFuelType().toIngredient().getMatchingFluidStacks(), FluidAttributes.BUCKET_VOLUME).toList();
		layout.addSlot(RecipeIngredientRole.INPUT, 4, 28).addIngredients(ForgeTypes.FLUID_STACK, fluids);
	}

}
