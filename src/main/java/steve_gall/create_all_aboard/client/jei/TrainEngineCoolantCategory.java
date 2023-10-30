package steve_gall.create_all_aboard.client.jei;

import com.simibubi.create.foundation.fluid.FluidIngredient;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.create_all_aboard.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_all_aboard.common.init.ModRecipeTypes;
import steve_gall.create_all_aboard.common.util.NumberHelper;

public class TrainEngineCoolantCategory extends ModJEIRecipeCategory<TrainEngineCoolantRecipe>
{
	public static final RecipeType<TrainEngineCoolantRecipe> RECIPE_TYPE = ModJEIRecipeTypes.TRAIN_ENGINE_COOLANT;
	public static final ResourceLocation TEXTURE_BACKGROUND = ModJEI.texture(RECIPE_TYPE.getUid());
	public static final String TEXT_TITLE = ModJEI.translationKey(RECIPE_TYPE, "title");
	public static final String TEXT_COOLING = ModJEI.translationKey(RECIPE_TYPE, "cooling");

	private final IDrawable icon;

	public TrainEngineCoolantCategory(ModJEI plugin)
	{
		super(plugin, RECIPE_TYPE, plugin.getJeiHelpers().getGuiHelper().createDrawable(TEXTURE_BACKGROUND, 0, 0, 178, 20), TEXT_TITLE);

		this.icon = plugin.getJeiHelpers().getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.ICE));
	}

	@Override
	public IDrawable getIcon()
	{
		return this.icon;
	}

	@Override
	public net.minecraft.world.item.crafting.RecipeType<? extends TrainEngineCoolantRecipe> getCraftingRecipeType()
	{
		return ModRecipeTypes.TRAIN_ENGINE_COOLANT.get();
	}

	@Override
	public void draw(TrainEngineCoolantRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
	{
		super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;

		int textColor = 0x404040;
		guiGraphics.drawString(font, Component.translatable(TEXT_COOLING, NumberHelper.format(recipe.getCooling()) + " J"), 22, 6, textColor, false);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder layout, TrainEngineCoolantRecipe recipe, IFocusGroup focus)
	{
		IRecipeSlotBuilder ingrendientSlot = layout.addSlot(RecipeIngredientRole.INPUT, 2, 2);
		Ingredient item = recipe.getItemIngredient();
		FluidIngredient fluid = recipe.getFluidIngredient();

		if (item != null)
		{
			ingrendientSlot.addIngredients(item);
		}
		else if (fluid != null)
		{
			ingrendientSlot.addIngredients(ForgeTypes.FLUID_STACK, fluid.getMatchingFluidStacks());
			ingrendientSlot.setFluidRenderer(fluid.getRequiredAmount(), false, 16, 16);
		}

	}

}
