package steve_gall.create_trainwrecked.client.jei;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.create_trainwrecked.common.crafting.HeatStage;
import steve_gall.create_trainwrecked.common.crafting.TrainHeatSourceRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;
import steve_gall.create_trainwrecked.common.util.NumberHelper;

public class TrainHeatSourceCategory extends ModJEIRecipeCategory<TrainHeatSourceRecipe>
{
	public static final RecipeType<TrainHeatSourceRecipe> RECIPE_TYPE = ModJEIRecipeTypes.TRAIN_HEAT_SOURCE;
	public static final ResourceLocation TEXTURE_BACKGROUND = ModJEI.texture(RECIPE_TYPE.getUid());
	public static final String TEXT_TITLE = ModJEI.translationKey(RECIPE_TYPE, "title");

	private final IDrawable icon;

	public TrainHeatSourceCategory(ModJEI plugin)
	{
		super(plugin, RECIPE_TYPE, plugin.getJeiHelpers().getGuiHelper().createDrawable(TEXTURE_BACKGROUND, 0, 0, 178, 120), TEXT_TITLE);

		this.icon = plugin.getJeiHelpers().getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AllBlocks.BLAZE_BURNER.get()));
	}

	@Override
	public IDrawable getIcon()
	{
		return this.icon;
	}

	@Override
	public net.minecraft.world.item.crafting.RecipeType<? extends TrainHeatSourceRecipe> getCraftingRecipeType()
	{
		return ModRecipeTypes.TRAIN_HEAT_SOURCE.get();
	}

	@Override
	public void draw(TrainHeatSourceRecipe recipe, IRecipeSlotsView slotsView, PoseStack stack, double mouseX, double mouseY)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;

		int textX = 4;
		int textY = 2;
		int textColor = 0x404040;

		List<HeatStage> stages = recipe.getStages();
		HeatStage passiveStage = new HeatStage.Builder().passive().level(0).build();
		List<HeatStage> ingredientStages = new ArrayList<>();

		for (int i = 0; i < stages.size(); i++)
		{
			HeatStage stage = stages.get(i);
			HeatStage.IngredientType ingredientType = stage.getIngredientType();

			if (ingredientType == HeatStage.IngredientType.PASSIVE)
			{
				passiveStage = stage;
			}
			else
			{
				ingredientStages.add(stage);
			}

		}

		textY = this.drawHeatSource(stack, font, textX, textY, textColor, passiveStage);

		for (HeatStage stage : ingredientStages)
		{
			textY = this.drawHeatSource(stack, font, textX, textY, textColor, stage);
		}

	}

	private int drawHeatSource(PoseStack stack, Font font, int textX, int textY, int textColor, HeatStage stage)
	{
		HeatStage.IngredientType ingredientType = stage.getIngredientType();

		font.draw(stack, Component.translatable(ingredientType == HeatStage.IngredientType.PASSIVE ? "Passive" : "On item burned"), textX + 18, textY, textColor);
		textY += font.lineHeight;
		font.draw(stack, Component.translatable("Heat Level: " + stage.getLevel()), textX + 18, textY, textColor);
		textY += font.lineHeight;

		if (stage.getLevel() > 0)
		{
			font.draw(stack, Component.translatable("Speed Limit: %s m/s", NumberHelper.format(stage.getSpeedLimit(), 2)), textX + 18, textY, textColor);
		}

		textY += font.lineHeight;
		textY += 9;
		return textY;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder layout, TrainHeatSourceRecipe recipe, IFocusGroup focus)
	{
		layout.addSlot(RecipeIngredientRole.INPUT, 2, 2).addItemStacks(ItemTagEntry.TYPE.getIngredientMatchingStacks(recipe.getBlocks()).toList());

		List<HeatStage> stages = recipe.getStages();
		int row = 0;

		for (HeatStage stage : stages)
		{
			HeatStage.IngredientType ingredientType = stage.getIngredientType();

			if (ingredientType == HeatStage.IngredientType.PASSIVE)
			{
				continue;
			}

			IRecipeSlotBuilder ingredientSlot = layout.addSlot(RecipeIngredientRole.CATALYST, 2, 38 + row * (27 + 9));
			ingredientSlot.setBackground(this.getPlugin().getJeiHelpers().getGuiHelper().getSlotDrawable(), -1, -1);

			if (ingredientType == HeatStage.IngredientType.BURN_TIME)
			{
				ingredientSlot.addItemStacks(this.getPlugin().getBurnableItems().stream().toList());
			}
			else if (ingredientType == HeatStage.IngredientType.BLAZE_BURNER_FUEL)
			{
				ingredientSlot.addItemStacks(this.getPlugin().getBlazeBunerFuels().stream().toList());
			}
			else if (ingredientType == HeatStage.IngredientType.SPECIFY)
			{
				ingredientSlot.addIngredients(stage.getSpecifiedIngredient());
			}

			row++;
		}

	}

}
