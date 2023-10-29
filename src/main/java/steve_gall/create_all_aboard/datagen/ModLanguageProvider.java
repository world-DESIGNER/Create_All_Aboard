package steve_gall.create_all_aboard.datagen;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderLocalization;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import steve_gall.create_all_aboard.client.content.train.StationScreenHelper;
import steve_gall.create_all_aboard.client.jei.TrainEngineCoolantCategory;
import steve_gall.create_all_aboard.client.jei.TrainEngineTypeCategory;
import steve_gall.create_all_aboard.client.jei.TrainHeatSourceCategory;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.content.train.TrainHelper;
import steve_gall.create_all_aboard.common.init.ModItems;
import steve_gall.create_all_aboard.common.item.JerrycanItem;

public class ModLanguageProvider extends LanguageProvider
{
	public ModLanguageProvider(DataGenerator gen)
	{
		super(gen, CreateAllAboard.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		this.add(ModItems.TRAIN_STEAM_ENGINE.get(), "Weathered Steam Engine");
		this.add(ModItems.TRAIN_STEAM_ENGINE.get().getDescriptionId() + ".tooltip", "Weathered Steam Engine");
		this.add(ModItems.TRAIN_STEAM_ENGINE.get().getDescriptionId() + ".tooltip.summary", "It's so weathered that it can only be used on trains");

		this.add(ModItems.JERRYCAN.get(), "Jerrycan");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip", "Jerrycan");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.empty", "Empty");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.amount", "%1$s: %2$s");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.capacity", "Capacity: %s");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.summary", "Portable fluid carriage for your fuel exhaustion emergency.");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.condition1", "When used on Fluid Interface in Train Carraige");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.behaviour1", "_Drain fluid_ from clicked carriage's fluid tanks. _Sneak_ while interacting to _fill fluid_ fluid tanks.");

		this.add(JerrycanItem.TOOLTIP_FILLED, "%1$s %2$s filled");
		this.add(JerrycanItem.TOOLTIP_DRAINED, "%1$s %2$s drained");
		this.add(JerrycanItem.TOOLTIP_ITEM_EMPTY, "Jerrycan is empty");
		this.add(JerrycanItem.TOOLTIP_ITEM_FULL, "Can't drain fluid, jerrycan can't accept fluid");
		this.add(JerrycanItem.TOOLTIP_STORAGE_EMPTY, "Fluid storage is empty");
		this.add(JerrycanItem.TOOLTIP_STORAGE_FULL, "Can't fill fluid, fluid storage can't accept fluid");

		this.addJEI();

		this.addTrainAssemble();
		this.addTrainStation();
		this.addTrainGoggle();

		this.addPonder();
	}

	private void addTrainAssemble()
	{
		this.add(TrainHelper.TRAIN_ASSEMBLEY_NO_ENGINES, "Engine Not Found");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_TOO_MANY_CARRIAGES, "Too Many Carriages, With the current engines, up to %s can be configured, Current carriages: %s");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_TOO_MANY_BLOCKS, "Too Many Contraption Blocks, With the current engines, up to %s can be placed, Current blocks: %s");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_NO_FLUID_INTERFACES, "Requires least one carriage have both %1$s and %2$s.");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_NO_HEAT_SOURCES, "Heat Source Not Found, to boil water requires at least one heat source, Only heat sources below the fluid tanks are detected.");
	}

	private void addTrainStation()
	{
		this.addTrainStation(StationScreenHelper.STATION_ENGINE_HAS_HEAT, "Cannot disassemble.", "need to cool the engines");
	}

	private void addTrainStation(String key, String title, String description)
	{
		this.add(Create.ID + ".station." + key, title);
		this.add(Create.ID + ".station." + key + "_1", description);
	}

	private void addTrainGoggle()
	{
		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED, "Overheated");
		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED_1, "It appears that this train's engine is overheated.");
		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED_2, "Use ice or wait %ss to cool down the engine.");

		this.add(TrainHelper.TRAIN_GOGGLE_TRAIN_INFO, "Train Info:");
		this.add(TrainHelper.TRAIN_GOGGLE_TRAIN_SPEED, "Speed: %1$s / %2$s");
		this.add(TrainHelper.TRAIN_GOGGLE_TRAIN_BLOCKS, "Blocks: %1$s / %2$s");
		this.add(TrainHelper.TRAIN_GOGGLE_HEAT_SOURCE_INFO, "Heat Source Info:");
		this.add(TrainHelper.TRAIN_GOGGLE_HEAT_SOURCE_LEVEL, "Level: %1$s / %2$s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_INFO, "Engine Info:");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_COUNT, "%1$s: %2$s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_TEMP, "Temp.: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_HIGHEST_TEMP, "Highest Temp.: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_OVERHEATEDS, "Overheateds: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_FLUID_CAPACITY, "Capacity: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_FLUID_AMOUNT, "%1$s: %2$s, %3$s");
	}

	private void addJEI()
	{
		this.add(TrainEngineTypeCategory.TEXT_TITLE, "Train Engine Type");
		this.add(TrainEngineTypeCategory.TEXT_MAX_CARRIAGES, "Max. Carriags: %s carriags");
		this.add(TrainEngineTypeCategory.TEXT_MAX_BLOCKS_PER_CARRIAGE, "Max. Blocks %s blocks/carriage");
		this.add(TrainEngineTypeCategory.TEXT_MAX_SPEED, "Max. Speed: %s");
		this.add(TrainEngineTypeCategory.TEXT_ACCELERATION, "Acceleration: %s");
		this.add(TrainEngineTypeCategory.TEXT_MAX_FUEL_USAGE, "Max. Fuel Usage: %s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_LEVEL_FOR_MAX_SPEED, "Heat Level For Max. Speed: %s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_PER_FUEL, "Heat Per Fuel: %s");
		this.add(TrainEngineTypeCategory.TEXT_AIR_COOLING_RATE, "Air Cooling Rate: %s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_CAPACITY, "Heat Capacity: %s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_DURABILITY, "Heat Durability: %s");

		this.add(TrainEngineCoolantCategory.TEXT_TITLE, "Train Engine Coolant");
		this.add(TrainEngineCoolantCategory.TEXT_COOLING, "Cooling: %s");

		this.add(TrainHeatSourceCategory.TEXT_TITLE, "Train Heat Source");
	}

	private void addPonder()
	{
		PonderLocalization.generateSceneLang();
		JsonObject object = new JsonObject();
		PonderLocalization.record(CreateAllAboard.MOD_ID, object);

		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			this.add(entry.getKey(), entry.getValue().getAsString());
		}

	}

}
