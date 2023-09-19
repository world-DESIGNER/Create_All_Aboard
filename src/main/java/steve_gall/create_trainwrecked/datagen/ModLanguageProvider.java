package steve_gall.create_trainwrecked.datagen;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.ponder.PonderLocalization;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import steve_gall.create_trainwrecked.client.jei.TrainEngineCategory;
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

		this.add(TrainEngineCategory.TEXT_TITLE, "Train Engine");
		this.add(TrainEngineCategory.TEXT_MAX_BOGEY, "Max. Bogey: %s");
		this.add(TrainEngineCategory.TEXT_MAX_SPEED, "Max. Speed: %s blocks/sec");
		this.add(TrainEngineCategory.TEXT_ACCELERATION, "Acceleration: %s blocks/sec²");
		this.add(TrainEngineCategory.TEXT_MAX_FUEL_USAGE, "Max. Fuel Usage: %s mB/sec");
		this.add(TrainEngineCategory.TEXT_HEAT_DURABILITY, "Heat Durability: %s sec");

		this.add(TrainHelper.NO_ENGINES, "Engine Not Found");
		this.add(TrainHelper.TOO_MANY_BOGEYS, "Too Many Bogeys");

		PonderLocalization.generateSceneLang();
		JsonObject object = new JsonObject();
		PonderLocalization.record(CreateTrainwrecked.MOD_ID, object);

		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			this.add(entry.getKey(), entry.getValue().getAsString());
		}

	}

}
