package steve_gall.create_trainwrecked.datagen;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.ponder.PonderLocalization;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import steve_gall.create_trainwrecked.client.jei.TrainEngineCoolantCategory;
import steve_gall.create_trainwrecked.client.jei.TrainEngineTypeCategory;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;
import steve_gall.create_trainwrecked.common.init.ModItems;
import steve_gall.create_trainwrecked.common.item.JerrycanItem;

public class ModLanguageProvider extends LanguageProvider
{
	public ModLanguageProvider(DataGenerator gen)
	{
		super(gen, CreateTrainwrecked.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		this.add(ModItems.JERRYCAN.get(), "Jerrycan");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.empty", "Empty");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.amount", "%1$s: %2$s mB");
		this.add(ModItems.JERRYCAN.get().getDescriptionId() + ".tooltip.capacity", "Capacity: %s mB");

		this.add(JerrycanItem.TOOLTIP_FILLED, "%1$s %2$s mB filled");
		this.add(JerrycanItem.TOOLTIP_DRAINED, "%1$s %2$s mB drained");
		this.add(JerrycanItem.TOOLTIP_ITEM_EMPTY, "Jerrycan is empty");
		this.add(JerrycanItem.TOOLTIP_ITEM_FULL, "Can't drain fluid, jerrycan can't accept fluid");
		this.add(JerrycanItem.TOOLTIP_STORAGE_EMPTY, "Fluid storage is empty");
		this.add(JerrycanItem.TOOLTIP_STORAGE_FULL, "Can't fill fluid, fluid storage can't accept fluid");

		this.addJEI();

		this.add(TrainHelper.TRAIN_ASSEMBLEY_NO_ENGINES, "Engine Not Found");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_TOO_MANY_CARRIAGES, "Too Many Carriages, With the current engines, up to %s can be configured, Current carriages: %s");
		this.add(TrainHelper.TRAIN_ASSEMBLEY_TOO_MANY_BLOCKS, "Too Many Contraption Blocks, With the current engines, up to %s can be placed, Current blocks: %s");

		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED, "Overheated");
		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED_1, "It appears that this train's engine is overheated.");
		this.add(TrainHelper.TRAIN_GOGGLE_OVERHEATED_2, "Use ice or wait %ss to cool down the engine.");

		this.add(TrainHelper.TRAIN_GOGGLE_TRAIN_INFO, "Train Info:");
		this.add(TrainHelper.TRAIN_GOGGLE_TRAIN_SPEED, "Speed: %1$s / %2$s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_INFO, "Engine Info:");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_TEMP, "Temp.: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_HIGHEST_TEMP, "Highest Temp.: %s");
		this.add(TrainHelper.TRAIN_GOGGLE_ENGINE_OVERHEATEDS, "Overheateds: %s");

		PonderLocalization.generateSceneLang();
		JsonObject object = new JsonObject();
		PonderLocalization.record(CreateTrainwrecked.MOD_ID, object);

		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			this.add(entry.getKey(), entry.getValue().getAsString());
		}

	}

	private void addJEI()
	{
		this.add(TrainEngineTypeCategory.TEXT_TITLE, "Train Engine Type");
		this.add(TrainEngineTypeCategory.TEXT_MAX_CARRIAGES, "Max. Carriags: %s");
		this.add(TrainEngineTypeCategory.TEXT_MAX_BLOCKS_PER_CARRIAGE, "Max. Blocks %s m/carriage");
		this.add(TrainEngineTypeCategory.TEXT_MAX_SPEED, "Max. Speed: %s m/s");
		this.add(TrainEngineTypeCategory.TEXT_ACCELERATION, "Acceleration: %s m/s²");
		this.add(TrainEngineTypeCategory.TEXT_MAX_FUEL_USAGE, "Max. Fuel Usage: %s mB/s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_PER_FUEL, "Heat Per Fuel: %s J/mB");
		this.add(TrainEngineTypeCategory.TEXT_AIR_COOLING_RATE, "Air Cooling Rate: %s J/s");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_CAPACITY, "Heat Capacity: %s J");
		this.add(TrainEngineTypeCategory.TEXT_HEAT_DURABILITY, "Heat Durability: %s s");

		this.add(TrainEngineCoolantCategory.TEXT_TITLE, "Train Engine Coolant");
		this.add(TrainEngineCoolantCategory.TEXT_COOLING, "Cooling: %s J");
	}

}
