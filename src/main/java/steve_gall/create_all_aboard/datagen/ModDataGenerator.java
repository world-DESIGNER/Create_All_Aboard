package steve_gall.create_all_aboard.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

		generator.addProvider(event.includeServer(), new ModLanguageProvider(packOutput));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(packOutput));
		generator.addProvider(event.includeServer(), new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
		ModProcessingRecipeProvider.run(generator);
	}

}
