package steve_gall.create_trainwrecked.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.create_trainwrecked.client.init.ModPonders;

public class CreateTrainwreckedClient
{
	public static void init()
	{
		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(CreateTrainwreckedClient::clientSetup);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
	}

	public static void clientSetup(FMLClientSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			ModPonders.register();
		});

	}

}
