package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import steve_gall.create_all_aboard.client.init.ModPonders;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		ModPonders.init();

		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(generator, existingFileHelper));

		generator.addProvider(event.includeServer(), new ModLanguageProvider(generator));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(generator));
		ModBlockTagProvider blocktagProvider = new ModBlockTagProvider(generator, existingFileHelper);
		generator.addProvider(event.includeServer(), blocktagProvider);
		generator.addProvider(event.includeServer(), new ModItemTagProvider(generator, blocktagProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModFluidTagProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
		ModProcessingRecipeProvider.run(generator);
	}

}
