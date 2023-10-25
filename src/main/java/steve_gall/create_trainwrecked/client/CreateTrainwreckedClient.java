package steve_gall.create_trainwrecked.client;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.create_trainwrecked.client.content.train.StationScreenHelper;
import steve_gall.create_trainwrecked.client.init.ModPonders;
import steve_gall.create_trainwrecked.client.instancing.TrainSteamEngineInstance;
import steve_gall.create_trainwrecked.client.mixin.train.StationScreenAcessor;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;
import steve_gall.create_trainwrecked.common.init.ModBlockEntityTypes;
import steve_gall.create_trainwrecked.common.mixin.train.StationBlockEntityAccessor;

public class CreateTrainwreckedClient
{
	public static void init()
	{
		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(CreateTrainwreckedClient::clientSetup);
		fml_bus.addListener(CreateTrainwreckedClient::registerClientReloadListeners);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(CreateTrainwreckedClient::clientTick);
		forge_bus.addListener(CreateTrainwreckedClient::clientLevelTick);

	}

	public static void clientSetup(FMLClientSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			ModPonders.init();

			InstancedRenderRegistry.configure(ModBlockEntityTypes.TRAIN_STEAM_ENGINE.get()).factory(TrainSteamEngineInstance::new).skipRender(be -> false).apply();
		});

	}

	public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(new ClientResourceReloadListener());
	}

	public static void clientTick(ClientTickEvent e)
	{
		if (e.phase == Phase.END && e.side == LogicalSide.CLIENT)
		{
			Minecraft minecraft = Minecraft.getInstance();

			if (minecraft.screen instanceof StationScreenAcessor accessor)
			{
				StationBlockEntity blockEntity = accessor.getBlockEntity();
				StationBlockEntityAccessor stationAccessor = (StationBlockEntityAccessor) blockEntity;
				TrackTargetingBehaviour<GlobalStation> edgePoint = blockEntity.edgePoint;

				if (!edgePoint.isOnCurve() && edgePoint.isOrthogonal() && accessor.invokeTrainPresent())
				{
					GlobalStation station = blockEntity.getStation();

					if (station != null && !stationAccessor.isTrainCanDisassemble())
					{
						Train train = accessor.invokeGetImminent();

						if (train != null && TrainHelper.anyEngineHasHeat(train))
						{
							accessor.invokeUpdateAssemblyTooltip(StationScreenHelper.STATION_ENGINE_HAS_HEAT);
						}

					}

				}

			}

		}

	}

	public static void clientLevelTick(LevelTickEvent e)
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
