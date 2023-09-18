package steve_gall.create_trainwrecked.datagen;

import java.util.function.Consumer;

import com.drmangotea.createindustry.blocks.CIBlocks;
import com.simibubi.create.AllBlocks;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.crafting.conditions.ICondition;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.recipe.ConditionFinishedRecipe;
import steve_gall.create_trainwrecked.common.recipe.FluidTagEntry;
import steve_gall.create_trainwrecked.common.recipe.ItemTagEntry;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class ModRecipeProvider extends RecipeProvider
{
	public ModRecipeProvider(DataGenerator pGenerator)
	{
		super(pGenerator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		TrainEngineRecipe.Builder steam = new TrainEngineRecipe.Builder();
		steam.blockType(ItemTagEntry.TYPE.of(AllBlocks.STEAM_ENGINE.get()));
		steam.maxSpeed(30.0F);
		steam.acceleration(steam.maxSpeed() / 60.0F);
		steam.fuelType(FluidTagEntry.TYPE.of(FluidTags.WATER));
		steam.fuelShare(true);
		steam.fuelMinimum(6480.0F / 60.0F);
		steam.fuelPerRecipePow(2.0F);
		steam.airCoolingRate(20);
		steam.heatCapacity((int) (((steam.maxSpeed() * steam.fuelPerSpeed() * steam.heatPerFuel()) - steam.airCoolingRate()) * 2 * 60));
		steam.burnOutDuration(5 * 60 * 20);
		pFinishedRecipeConsumer.accept(this.wrapCondition(steam.build(CreateTrainwrecked.asResource("train/engines/steam")).finish()));

		TrainEngineRecipe.Builder diesel = new TrainEngineRecipe.Builder();
		diesel.blockType(ItemTagEntry.TYPE.of(CIBlocks.DIESEL_ENGINE.get()));
		diesel.maxSpeed(50.0F);
		diesel.acceleration(diesel.maxSpeed() / 5.0F);
		diesel.fuelType(FluidTagEntry.TYPE.of(FluidTags.create(new ResourceLocation("forge", "diesel"))));
		diesel.fuelPerSpeed(1.0F);
		diesel.heatPerFuel(10);
		diesel.airCoolingRate(20);
		diesel.heatCapacity((int) (((diesel.maxSpeed() * diesel.fuelPerSpeed() * diesel.heatPerFuel()) - diesel.airCoolingRate()) * 5 * 60));
		diesel.burnOutDuration(5 * 60 * 20);
		pFinishedRecipeConsumer.accept(this.wrapCondition(diesel.build(CreateTrainwrecked.asResource("train/engines/diesel")).finish()));
	}

	public ConditionFinishedRecipe wrapCondition(FinishedRecipe recipe, ICondition... conditions)
	{
		return new ConditionFinishedRecipe(recipe, conditions);
	}

}
