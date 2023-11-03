package steve_gall.create_all_aboard.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
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
		generator.addProvider(new ModBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));

		generator.addProvider(new ModLootTableProvider(generator));
		ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(generator, existingFileHelper);
		generator.addProvider(blockTagProvider);
		generator.addProvider(new ModItemTagProvider(generator, blockTagProvider, existingFileHelper));
		generator.addProvider(new ModFluidTagProvider(generator, existingFileHelper));
		ModRecipeProvider recipeProvider = new ModRecipeProvider(generator);
		generator.addProvider(recipeProvider);
		ModProcessingRecipeProvider.run(generator);

		generator.addProvider(new ModLanguageProvider(generator, recipeProvider));
	}

}
