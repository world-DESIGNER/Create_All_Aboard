package steve_gall.create_trainwrecked.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.drmangotea.createindustry.blocks.CIBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.fluids.FluidAttributes;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.ConditionFinishedRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;
import steve_gall.create_trainwrecked.common.util.WrappedTagEntry;

public class ModRecipeProvider extends RecipeProvider
{
	private final String modId;

	public ModRecipeProvider(DataGenerator pGenerator)
	{
		super(pGenerator);
		this.modId = CreateTrainwrecked.MOD_ID;
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		this.engineTypes(pFinishedRecipeConsumer);
		this.engineCoolings(pFinishedRecipeConsumer);
	}

	private void engineTypes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		TrainEngineTypeRecipe.Builder<?> steam = new TrainEngineTypeRecipe.Builder<>();
		steam.blockType(ItemTagEntry.TYPE.of(AllBlocks.STEAM_ENGINE.get()));
		steam.maxSpeed(50.0F);
		steam.acceleration(steam.maxSpeed() / 60.0F);
		steam.carriageStressMultiplier(0.75F);
		steam.fuelType(FluidTagEntry.TYPE.of(Fluids.WATER));
		steam.fuelShare(true);
		steam.fuelMinimum(6480.0F / 60.0F);
		steam.fuelPerRecipePow(2.0F);
		steam.airCoolingRate(20);
		steam.heatCapacity((int) (((steam.maxSpeed() * steam.fuelPerSpeed() * steam.heatPerFuel()) - steam.airCoolingRate()) * 2 * 60));
		steam.overheatDuration(5 * 60 * 20);
		this.save(pFinishedRecipeConsumer, steam, "steam");

		TrainEngineTypeRecipe.Builder<?> diesel = new TrainEngineTypeRecipe.Builder<>();
		diesel.blockType(ItemTagEntry.TYPE.of(CIBlocks.DIESEL_ENGINE.get()));
		diesel.maxSpeed(30.0F);
		diesel.acceleration(diesel.maxSpeed() / 5.0F);
		diesel.fuelType(FluidTagEntry.TYPE.of(FluidTags.create(new ResourceLocation("forge", "diesel"))));
		diesel.fuelPerSpeed(1.0F);
		diesel.heatPerFuel(10);
		diesel.airCoolingRate(20);
		diesel.heatCapacity((int) (((diesel.maxSpeed() * diesel.fuelPerSpeed() * diesel.heatPerFuel()) - diesel.airCoolingRate()) * 5 * 60));
		diesel.overheatDuration(5 * 60 * 20);
		this.save(pFinishedRecipeConsumer, diesel, "diesel");
	}

	public void save(Consumer<FinishedRecipe> consumer, TrainEngineTypeRecipe.Builder<?> builder, String name)
	{
		FinishedRecipe finish = builder.finish(new ResourceLocation(this.modId, "train/engines/" + name));
		List<ICondition> conditions = new ArrayList<>();

		WrappedTagEntry blockType = builder.blockType().getTagEntry();
		if (!blockType.isTag() && !blockType.id().getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE))
		{
			conditions.add(new ItemExistsCondition(blockType.id()));
		}

		consumer.accept(new ConditionFinishedRecipe(finish, conditions));
	}

	private void engineCoolings(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		TrainEngineCoolantRecipe.Builder<?> water = new TrainEngineCoolantRecipe.Builder<>();
		water.fluidIngredient(FluidIngredient.fromFluid(Fluids.WATER, 1));
		water.cooling(1);
		this.save(pFinishedRecipeConsumer, water, "fluid_water");

		TrainEngineCoolantRecipe.Builder<?> snow_block = new TrainEngineCoolantRecipe.Builder<>();
		snow_block.itemIngredient(Ingredient.of(Blocks.SNOW_BLOCK));
		snow_block.cooling(water.cooling() * FluidAttributes.BUCKET_VOLUME / water.fluidIngredient().getRequiredAmount());
		this.save(pFinishedRecipeConsumer, snow_block, "item_snow_block");

		TrainEngineCoolantRecipe.Builder<?> snowball = new TrainEngineCoolantRecipe.Builder<>();
		snowball.itemIngredient(Ingredient.of(Items.SNOWBALL));
		snowball.cooling(snow_block.cooling() / 4);
		this.save(pFinishedRecipeConsumer, snowball, "item_snowball");

		TrainEngineCoolantRecipe.Builder<?> snow = new TrainEngineCoolantRecipe.Builder<>();
		snow.itemIngredient(Ingredient.of(Blocks.SNOW));
		snow.cooling(snow_block.cooling() / 2);
		this.save(pFinishedRecipeConsumer, snow, "item_snow");

		TrainEngineCoolantRecipe.Builder<?> ice = new TrainEngineCoolantRecipe.Builder<>();
		ice.itemIngredient(Ingredient.of(Items.ICE));
		ice.cooling(water.cooling() * FluidAttributes.BUCKET_VOLUME / water.fluidIngredient().getRequiredAmount());
		this.save(pFinishedRecipeConsumer, ice, "item_ice");

		TrainEngineCoolantRecipe.Builder<?> packed_ice = new TrainEngineCoolantRecipe.Builder<>();
		packed_ice.itemIngredient(Ingredient.of(Items.PACKED_ICE));
		packed_ice.cooling(ice.cooling() * 10);
		this.save(pFinishedRecipeConsumer, packed_ice, "item_packed_ice");

		TrainEngineCoolantRecipe.Builder<?> blue_ice = new TrainEngineCoolantRecipe.Builder<>();
		blue_ice.itemIngredient(Ingredient.of(Items.BLUE_ICE));
		blue_ice.cooling(packed_ice.cooling() * 10);
		this.save(pFinishedRecipeConsumer, blue_ice, "item_blue_ice");
	}

	public void save(Consumer<FinishedRecipe> consumer, TrainEngineCoolantRecipe.Builder<?> builder, String name)
	{
		FinishedRecipe finish = builder.finish(new ResourceLocation(this.modId, "train/coolants/" + name));
		consumer.accept(finish);
	}

}
