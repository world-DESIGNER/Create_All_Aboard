package steve_gall.create_trainwrecked.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.drmangotea.createindustry.registry.CIBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.fluids.FluidType;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.crafting.ConditionFinishedRecipe;
import steve_gall.create_trainwrecked.common.crafting.CrashBehavior;
import steve_gall.create_trainwrecked.common.crafting.HeatStage;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineCoolantRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.crafting.TrainHeatSourceRecipe;
import steve_gall.create_trainwrecked.common.init.ModBlocks;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

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
		this.engineCoolants(pFinishedRecipeConsumer);
		this.heatSources(pFinishedRecipeConsumer);
	}

	public void engineTypes(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		TrainEngineTypeRecipe.Builder<?> steam = new TrainEngineTypeRecipe.Builder<>();
		steam.blockType(ItemTagEntry.TYPE.of(AllBlocks.STEAM_ENGINE.get()));
		steam.blockType(ItemTagEntry.TYPE.of(ModBlocks.TRAIN_STEAM_ENGINE.get()));
		steam.maxSpeed(50.0F);
		steam.acceleration(steam.maxSpeed() / 60.0F);
		steam.carriageStressMultiplier(0.75D);
		steam.fuelType(FluidTagEntry.TYPE.of(Fluids.WATER));
		steam.limitableByHeat(true);
		steam.fuelPerSpeed(1.0D);
		steam.fuelPerHeatLevel(FluidType.BUCKET_VOLUME / 267.6D);
		steam.overheatedResettingTemp(0.0D);
		steam.heatCapacity(0);
		steam.crashBehavior(new CrashBehavior.Builder().explosionRadius(5.0F).causesFire(true).explosionMode(BlockInteraction.NONE).build());
		this.save(pFinishedRecipeConsumer, steam, "steam");

		TrainEngineTypeRecipe.Builder<?> diesel = new TrainEngineTypeRecipe.Builder<>();
		diesel.blockType(ItemTagEntry.TYPE.of(CIBlocks.DIESEL_ENGINE.get()));
		diesel.maxSpeed(40.0F);
		diesel.acceleration(diesel.maxSpeed() / 5.0F);
		diesel.fuelType(FluidTagEntry.TYPE.of(FluidTags.create(new ResourceLocation("forge", "diesel"))));
		diesel.fuelType(FluidTagEntry.TYPE.of(FluidTags.create(new ResourceLocation("forge", "biodiesel"))));
		diesel.fuelPerSpeed(8000.0D / 1800.0D / 40.0D / 2.0D);
		diesel.heatPerFuel(27.0D);
		diesel.overheatedResettingTemp(0.1D);
		this.solveHeatVariables(diesel, 10 * 60, 5 * 60);
		this.save(pFinishedRecipeConsumer, diesel, "diesel");
	}

	public void solveHeatVariables(TrainEngineTypeRecipe.Builder<?> engineType, double airCoolingDurationToZero, double heatDurability)
	{
		double fuelUsage = engineType.maxSpeed() * engineType.fuelPerSpeed() * engineType.heatPerFuel();
		double denominator = 1.0D + (airCoolingDurationToZero / heatDurability);
		engineType.airCoolingRate(fuelUsage / denominator);
		engineType.heatCapacity((int) (heatDurability * (fuelUsage - engineType.airCoolingRate())));
	}

	public void save(Consumer<FinishedRecipe> consumer, TrainEngineTypeRecipe.Builder<?> builder, String name)
	{
		FinishedRecipe finish = builder.finish(new ResourceLocation(this.modId, "train/engines/" + name));
		List<ICondition> conditions = new ArrayList<>();
		this.addBlockExistConditions(builder.blockType(), conditions);

		consumer.accept(new ConditionFinishedRecipe(finish, conditions));
	}

	public void engineCoolants(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		int heatPerSec = 40;

		TrainEngineCoolantRecipe.Builder<?> water = new TrainEngineCoolantRecipe.Builder<>();
		water.fluidIngredient(FluidIngredient.fromFluid(Fluids.WATER, 1));
		water.cooling((int) (heatPerSec * 0.1D));
		this.save(pFinishedRecipeConsumer, water, "fluid_water");

		TrainEngineCoolantRecipe.Builder<?> snowball = new TrainEngineCoolantRecipe.Builder<>();
		snowball.itemIngredient(Ingredient.of(Items.SNOWBALL));
		snowball.cooling((int) (heatPerSec * 1.0D));
		this.save(pFinishedRecipeConsumer, snowball, "item_snowball");

		TrainEngineCoolantRecipe.Builder<?> snow = new TrainEngineCoolantRecipe.Builder<>();
		snow.itemIngredient(Ingredient.of(Blocks.SNOW));
		snow.cooling((int) (heatPerSec * 2.0D));
		this.save(pFinishedRecipeConsumer, snow, "item_snow");

		TrainEngineCoolantRecipe.Builder<?> snow_block = new TrainEngineCoolantRecipe.Builder<>();
		snow_block.itemIngredient(Ingredient.of(Blocks.SNOW_BLOCK));
		snow_block.cooling((int) (heatPerSec * 4.0));
		this.save(pFinishedRecipeConsumer, snow_block, "item_snow_block");

		TrainEngineCoolantRecipe.Builder<?> ice = new TrainEngineCoolantRecipe.Builder<>();
		ice.itemIngredient(Ingredient.of(Items.ICE));
		ice.cooling((int) (heatPerSec * 10.0D));
		this.save(pFinishedRecipeConsumer, ice, "item_ice");

		TrainEngineCoolantRecipe.Builder<?> packed_ice = new TrainEngineCoolantRecipe.Builder<>();
		packed_ice.itemIngredient(Ingredient.of(Items.PACKED_ICE));
		packed_ice.cooling((int) (heatPerSec * 30.0D));
		this.save(pFinishedRecipeConsumer, packed_ice, "item_packed_ice");

		TrainEngineCoolantRecipe.Builder<?> blue_ice = new TrainEngineCoolantRecipe.Builder<>();
		blue_ice.itemIngredient(Ingredient.of(Items.BLUE_ICE));
		blue_ice.cooling((int) (heatPerSec * 60.0D));
		this.save(pFinishedRecipeConsumer, blue_ice, "item_blue_ice");
	}

	public void save(Consumer<FinishedRecipe> consumer, TrainEngineCoolantRecipe.Builder<?> builder, String name)
	{
		FinishedRecipe finish = builder.finish(new ResourceLocation(this.modId, "train/coolants/" + name));
		List<ICondition> conditions = new ArrayList<>();
		consumer.accept(new ConditionFinishedRecipe(finish, conditions));
	}

	public void heatSources(Consumer<FinishedRecipe> pFinishedRecipeConsumer)
	{
		TrainHeatSourceRecipe.Builder<?> passive = new TrainHeatSourceRecipe.Builder<>();
		passive.blockType(ItemTagEntry.TYPE.of(Items.CAMPFIRE));
		passive.blockType(ItemTagEntry.TYPE.of(Items.MAGMA_BLOCK));
		passive.stage(new HeatStage.Builder().level(1).passive().build());
		this.save(pFinishedRecipeConsumer, passive, "passive");

		TrainHeatSourceRecipe.Builder<?> charcoal_burner = new TrainHeatSourceRecipe.Builder<>();
		charcoal_burner.blockType(ItemTagEntry.TYPE.of(zeh.createlowheated.AllBlocks.CHARCOAL_BURNER.get()));
		charcoal_burner.stage(new HeatStage.Builder().level(1).burnTime().build());
		this.save(pFinishedRecipeConsumer, charcoal_burner, "charcoal_burner");

		TrainHeatSourceRecipe.Builder<?> blaze_burner = new TrainHeatSourceRecipe.Builder<>();
		blaze_burner.blockType(ItemTagEntry.TYPE.of(AllBlocks.BLAZE_BURNER.get()));
		blaze_burner.stage(new HeatStage.Builder().level(1).passive().build());
		blaze_burner.stage(new HeatStage.Builder().level(2).burnTime().build());
		blaze_burner.stage(new HeatStage.Builder().level(3).blazeBurnerFuel().build());
		this.save(pFinishedRecipeConsumer, blaze_burner, "blaze_burner");
	}

	public void save(Consumer<FinishedRecipe> consumer, TrainHeatSourceRecipe.Builder<?> builder, String name)
	{
		FinishedRecipe finish = builder.finish(new ResourceLocation(this.modId, "train/heat_sources/" + name));
		List<ICondition> conditions = new ArrayList<>();
		this.addBlockExistConditions(builder.blockType(), conditions);

		consumer.accept(new ConditionFinishedRecipe(finish, conditions));
	}

	private void addBlockExistConditions(List<ItemTagEntry> list, List<ICondition> conditions)
	{
		for (ItemTagEntry blockType : list)
		{
			this.addBlockExistConditions(conditions, blockType);
		}

	}

	private void addBlockExistConditions(List<ICondition> conditions, ItemTagEntry blockType)
	{
		TagEntry tagEntry = blockType.getTagEntry();
		List<String> builtinNamespaces = Arrays.asList(ResourceLocation.DEFAULT_NAMESPACE, Create.ID, CreateTrainwrecked.MOD_ID);

		if (!tagEntry.isTag() && !builtinNamespaces.contains(tagEntry.getId().getNamespace()))
		{
			conditions.add(new ItemExistsCondition(tagEntry.getId()));
		}

	}

}
