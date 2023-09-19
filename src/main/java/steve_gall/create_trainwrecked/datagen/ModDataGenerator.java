package steve_gall.create_trainwrecked.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import steve_gall.create_trainwrecked.client.init.ModPonders;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		ModPonders.register();

		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));

		generator.addProvider(new ModLanguageProvider(generator));
		generator.addProvider(new ModRecipeProvider(generator));
	}

}
