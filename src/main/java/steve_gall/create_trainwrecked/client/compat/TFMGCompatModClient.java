package steve_gall.create_trainwrecked.client.compat;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class TFMGCompatModClient
{
	public static void init()
	{
		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(TFMGCompatModClient::clientSetup);
	}

	public static void clientSetup(FMLClientSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			TFMGCompatPonders.init();
		});

	}

}
