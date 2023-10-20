package steve_gall.create_trainwrecked.client;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.create_trainwrecked.client.init.ModPonders;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

public class CreateTrainwreckedClient
{
	public static void init()
	{
		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(CreateTrainwreckedClient::clientSetup);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(CreateTrainwreckedClient::clientTick);
	}

	public static void clientSetup(FMLClientSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			ModPonders.init();
		});

	}

	public static void clientTick(LevelTickEvent e)
	{
		if (e.phase == Phase.START && e.side == LogicalSide.CLIENT)
		{
			Level level = e.level;

			for (Train train : CreateClient.RAILWAYS.trains.values())
			{
				TrainHelper.tickTrainClient(train, level);
			}

		}

	}

}
