package steve_gall.create_all_aboard.common.content.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainPacket;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.graph.TrackNodeLocation.DiscoveredLocation;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.create_all_aboard.common.CreateAllAboard;
import steve_gall.create_all_aboard.common.content.contraption.MountedStorageManagerExtension;
import steve_gall.create_all_aboard.common.crafting.CrashBehavior;
import steve_gall.create_all_aboard.common.crafting.HeatStage;
import steve_gall.create_all_aboard.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_all_aboard.common.fluid.FluidHelper;
import steve_gall.create_all_aboard.common.mixin.contraption.ContraptionAccessor;
import steve_gall.create_all_aboard.common.mixin.contraption.MountedStorageManagerAccessor;
import steve_gall.create_all_aboard.common.mixin.train.StationBlockEntityAccessor;
import steve_gall.create_all_aboard.common.util.CompareUtil;
import steve_gall.create_all_aboard.common.util.FluidTagEntry;
import steve_gall.create_all_aboard.common.util.NumberHelper;

public class TrainHelper
{
	public static String TRAIN_ASSEMBLEY_NO_ENGINES = CreateAllAboard.translationKey("train_assembly.no_engines");
	public static String TRAIN_ASSEMBLEY_TOO_MANY_CARRIAGES = CreateAllAboard.translationKey("train_assembly.too_many_carriages");
	public static String TRAIN_ASSEMBLEY_TOO_MANY_BLOCKS = CreateAllAboard.translationKey("train_assembly.too_many_blocks");
	public static String TRAIN_ASSEMBLEY_NO_FLUID_INTERFACES = CreateAllAboard.translationKey("train_assembly.no_fluid_interfaces");
	public static String TRAIN_ASSEMBLEY_NO_HEAT_SOURCES = CreateAllAboard.translationKey("train_assembly.no_heat_sources");

	public static String TRAIN_GOGGLE_OVERHEATED = CreateAllAboard.translationKey("train_google.overheated");
	public static String TRAIN_GOGGLE_OVERHEATED_1 = CreateAllAboard.translationKey("train_google.overheated.1");
	public static String TRAIN_GOGGLE_OVERHEATED_2 = CreateAllAboard.translationKey("train_google.overheated.2");

	public static String TRAIN_GOGGLE_TRAIN_INFO = CreateAllAboard.translationKey("train_google.train_info");
	public static String TRAIN_GOGGLE_TRAIN_SPEED = CreateAllAboard.translationKey("train_google.train_speed");
	public static String TRAIN_GOGGLE_TRAIN_BLOCKS = CreateAllAboard.translationKey("train_google.train_blocks");
	public static String TRAIN_GOGGLE_HEAT_SOURCE_INFO = CreateAllAboard.translationKey("train_google.heat_source_info");
	public static String TRAIN_GOGGLE_HEAT_SOURCE_LEVEL = CreateAllAboard.translationKey("train_google.heat_source_level");
	public static String TRAIN_GOGGLE_ENGINE_INFO = CreateAllAboard.translationKey("train_google.engine_info");
	public static String TRAIN_GOGGLE_ENGINE_COUNT = CreateAllAboard.translationKey("train_google.engine_count");
	public static String TRAIN_GOGGLE_ENGINE_TEMP = CreateAllAboard.translationKey("train_google.engine_temp");
	public static String TRAIN_GOGGLE_ENGINE_HIGHEST_TEMP = CreateAllAboard.translationKey("train_google.engine_highest_heat");
	public static String TRAIN_GOGGLE_ENGINE_OVERHEATEDS = CreateAllAboard.translationKey("train_google.engine_overheateds");
	public static String TRAIN_GOGGLE_FLUID_CAPACITY = CreateAllAboard.translationKey("train_google.fluid_capacity");
	public static String TRAIN_GOGGLE_FLUID_AMOUNT = CreateAllAboard.translationKey("train_google.fluid_amount");

	public static void assemble(StationBlockEntity self, UUID playerUUID)
	{
		self.refreshAssemblyInfo();

		StationBlockEntityAccessor accessor = (StationBlockEntityAccessor) self;
		int assemblyLength = accessor.getAssemblyLength();
		int[] bogeyLocations = accessor.getBogeyLocations();
		AbstractBogeyBlock<?>[] bogeyTypes = accessor.getBogeyTypes();
		Direction assemblyDirection = self.getAssemblyDirection();
		TrackTargetingBehaviour<GlobalStation> edgePoint = self.edgePoint;
		boolean[] upsideDownBogeys = accessor.getUpsideDownBogeys();
		int bogeyCount = accessor.getBogeyCount();
		Level level = self.getLevel();

		self.refreshAssemblyInfo();

		if (bogeyLocations == null)
			return;

		if (bogeyLocations[0] != 0)
		{
			accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.frontmost_bogey_at_station")), -1);
			return;
		}

		if (!edgePoint.hasValidTrack())
			return;

		BlockPos trackPosition = edgePoint.getGlobalPosition();
		BlockState trackState = edgePoint.getTrackBlockState();
		ITrackBlock track = edgePoint.getTrack();
		BlockPos bogeyOffset = new BlockPos(track.getUpNormal(level, trackPosition, trackState));

		TrackNodeLocation location = null;
		Vec3 centre = Vec3.atBottomCenterOf(trackPosition).add(0, track.getElevationAtCenter(level, trackPosition, trackState), 0);
		Collection<DiscoveredLocation> ends = track.getConnected(level, trackPosition, trackState, true, null);
		Vec3 targetOffset = Vec3.atLowerCornerOf(assemblyDirection.getNormal());
		for (DiscoveredLocation end : ends)
			if (Mth.equal(0, targetOffset.distanceToSqr(end.getLocation().subtract(centre).normalize())))
				location = end;
		if (location == null)
			return;

		List<Double> pointOffsets = new ArrayList<>();
		int iPrevious = -100;
		for (int i = 0; i < bogeyLocations.length; i++)
		{
			int loc = bogeyLocations[i];
			if (loc == -1)
				break;

			if (loc - iPrevious < 3)
			{
				accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.bogeys_too_close", i, i + 1)), -1);
				return;
			}

			double bogeySize = bogeyTypes[i].getWheelPointSpacing();
			pointOffsets.add(Double.valueOf(loc + .5 - bogeySize / 2));
			pointOffsets.add(Double.valueOf(loc + .5 + bogeySize / 2));
			iPrevious = loc;
		}

		List<TravellingPoint> points = new ArrayList<>();
		Vec3 directionVec = Vec3.atLowerCornerOf(assemblyDirection.getNormal());
		TrackGraph graph = null;
		TrackNode secondNode = null;
		int carriagesHavingTanksInterfaces = 0;

		for (int j = 0; j < assemblyLength * 2 + 40; j++)
		{
			double i = j / 2d;
			if (points.size() == pointOffsets.size())
				break;

			TrackNodeLocation currentLocation = location;
			location = new TrackNodeLocation(location.getLocation().add(directionVec.scale(.5))).in(location.dimension);

			if (graph == null)
				graph = Create.RAILWAYS.getGraph(level, currentLocation);
			if (graph == null)
				continue;
			TrackNode node = graph.locateNode(currentLocation);
			if (node == null)
				continue;

			for (int pointIndex = points.size(); pointIndex < pointOffsets.size(); pointIndex++)
			{
				double offset = pointOffsets.get(pointIndex);
				if (offset > i)
					break;
				double positionOnEdge = i - offset;

				Map<TrackNode, TrackEdge> connectionsFromNode = graph.getConnectionsFrom(node);

				if (secondNode == null)
					for (Entry<TrackNode, TrackEdge> entry : connectionsFromNode.entrySet())
					{
						TrackEdge edge = entry.getValue();
						TrackNode otherNode = entry.getKey();
						if (edge.isTurn())
							continue;
						Vec3 edgeDirection = edge.getDirection(true);
						if (Mth.equal(edgeDirection.normalize().dot(directionVec), -1d))
							secondNode = otherNode;
					}

				if (secondNode == null)
				{
					Create.LOGGER.warn("Cannot assemble: No valid starting node found");
					return;
				}

				TrackEdge edge = connectionsFromNode.get(secondNode);

				if (edge == null)
				{
					Create.LOGGER.warn("Cannot assemble: Missing graph edge");
					return;
				}

				points.add(new TravellingPoint(node, secondNode, edge, positionOnEdge, false));
			}

			secondNode = node;
		}

		if (points.size() != pointOffsets.size())
		{
			Create.LOGGER.warn("Cannot assemble: Not all Points created");
			return;
		}

		if (points.size() == 0)
		{
			accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.no_bogeys")), -1);
			return;
		}

		List<CarriageContraption> contraptions = new ArrayList<>();
		List<Carriage> carriages = new ArrayList<>();
		List<Integer> spacing = new ArrayList<>();
		List<Engine> assemblingEngines = new ArrayList<>();
		List<HeatSource> assemblingHeatSources = new ArrayList<>();
		boolean atLeastOneForwardControls = false;
		long totalBlocks = 0L;

		for (int bogeyIndex = 0; bogeyIndex < bogeyCount; bogeyIndex++)
		{
			int pointIndex = bogeyIndex * 2;
			if (bogeyIndex > 0)
				spacing.add(bogeyLocations[bogeyIndex] - bogeyLocations[bogeyIndex - 1]);
			CarriageContraption contraption = new CarriageContraption(assemblyDirection);
			BlockPos bogeyPosOffset = trackPosition.offset(bogeyOffset);
			BlockPos upsideDownBogeyPosOffset = trackPosition.offset(new BlockPos(bogeyOffset.getX(), bogeyOffset.getY() * -1, bogeyOffset.getZ()));

			try
			{
				int offset = bogeyLocations[bogeyIndex] + 1;
				boolean success = contraption.assemble(level, upsideDownBogeys[bogeyIndex] ? upsideDownBogeyPosOffset.relative(assemblyDirection, offset) : bogeyPosOffset.relative(assemblyDirection, offset));
				atLeastOneForwardControls |= contraption.hasForwardControls();
				CarriageContraptionExtension contraptionExtension = (CarriageContraptionExtension) contraption;
				assemblingEngines.addAll(contraptionExtension.getAssembledEngines());
				assemblingHeatSources.addAll(contraptionExtension.getAssembledHeatSources());

				contraption.setSoundQueueOffset(offset);
				if (!success)
				{
					accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.nothing_attached", bogeyIndex + 1)), -1);
					return;
				}
			}
			catch (AssemblyException e)
			{
				accessor.invokeException(e, contraptions.size() + 1);
				return;
			}

			AbstractBogeyBlock<?> typeOfFirstBogey = bogeyTypes[bogeyIndex];
			boolean firstBogeyIsUpsideDown = upsideDownBogeys[bogeyIndex];
			BlockPos firstBogeyPos = contraption.anchor;
			AbstractBogeyBlockEntity firstBogeyTileEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(firstBogeyPos);
			CarriageBogey firstBogey = new CarriageBogey(typeOfFirstBogey, firstBogeyIsUpsideDown, firstBogeyTileEntity.getBogeyData(), points.get(pointIndex), points.get(pointIndex + 1));
			CarriageBogey secondBogey = null;
			BlockPos secondBogeyPos = contraption.getSecondBogeyPos();
			int bogeySpacing = 0;

			if (secondBogeyPos != null)
			{
				if (bogeyIndex == bogeyCount - 1 || !secondBogeyPos.equals((upsideDownBogeys[bogeyIndex + 1] ? upsideDownBogeyPosOffset : bogeyPosOffset).relative(assemblyDirection, bogeyLocations[bogeyIndex + 1] + 1)))
				{
					accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.not_connected_in_order")), contraptions.size() + 1);
					return;
				}
				AbstractBogeyBlockEntity secondBogeyTileEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(secondBogeyPos);
				bogeySpacing = bogeyLocations[bogeyIndex + 1] - bogeyLocations[bogeyIndex];
				secondBogey = new CarriageBogey(bogeyTypes[bogeyIndex + 1], upsideDownBogeys[bogeyIndex + 1], secondBogeyTileEntity.getBogeyData(), points.get(pointIndex + 2), points.get(pointIndex + 3));
				bogeyIndex++;

			}
			else if (!typeOfFirstBogey.allowsSingleBogeyCarriage())
			{
				accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.single_bogey_carriage")), contraptions.size() + 1);
				return;
			}

			Map<BlockPos, MountedFluidStorage> fluids = ((MountedStorageManagerAccessor) ((ContraptionAccessor) contraption).getStorage()).getFluidStorage();

			if (fluids.size() > 0)
			{
				boolean hasFluidInterface = contraption.getBlocks().values().stream().anyMatch(sbi -> sbi.state.getBlock() == AllBlocks.PORTABLE_FLUID_INTERFACE.get());

				if (hasFluidInterface)
				{
					carriagesHavingTanksInterfaces++;
				}

			}

			totalBlocks += contraption.getBlocks().size();
			contraptions.add(contraption);
			carriages.add(new Carriage(firstBogey, secondBogey, bogeySpacing));
		}

		if (!atLeastOneForwardControls)
		{
			accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.no_controls")), -1);
			return;
		}

		if (assemblingEngines.size() == 0)
		{
			accessor.invokeException(new AssemblyException(Component.translatable(TRAIN_ASSEMBLEY_NO_ENGINES)), -1);
			return;
		}

		double totalEngineCarriageStressHeap = 0.0D;
		double maxBlocksPerCarriage = 0.0D;
		for (Engine engine : assemblingEngines)
		{
			totalEngineCarriageStressHeap += engine.getRecipe().getCarriageSpeedStressHeap();
			maxBlocksPerCarriage += engine.getRecipe().getMaxBlockCountPerCarriage();
		}

		int maxCarriageCount = (int) TrainEngineTypeRecipe.getMaxCarriageCount(totalEngineCarriageStressHeap);

		if (!Double.isInfinite(totalEngineCarriageStressHeap) && maxCarriageCount < carriages.size())
		{
			accessor.invokeException(new AssemblyException(Component.translatable(TRAIN_ASSEMBLEY_TOO_MANY_CARRIAGES, maxCarriageCount, carriages.size())), -1);
			return;
		}

		int maxBlocks = (int) (maxBlocksPerCarriage * carriages.size());

		if (!Double.isInfinite(maxBlocksPerCarriage) && maxBlocks < totalBlocks)
		{
			accessor.invokeException(new AssemblyException(Component.translatable(TRAIN_ASSEMBLEY_TOO_MANY_BLOCKS, maxBlocks, totalBlocks)), -1);
			return;
		}

		if (assemblingEngines.stream().allMatch(e -> e.getRecipe().isLimitableByHeat()))
		{
			if (assemblingHeatSources.size() == 0)
			{
				accessor.invokeException(new AssemblyException(Component.translatable(TRAIN_ASSEMBLEY_NO_HEAT_SOURCES)), -1);
				return;
			}
		}

		if (carriagesHavingTanksInterfaces == 0)
		{
			accessor.invokeException(new AssemblyException(Component.translatable(TRAIN_ASSEMBLEY_NO_FLUID_INTERFACES, AllBlocks.FLUID_TANK.asStack().getHoverName(), AllBlocks.PORTABLE_FLUID_INTERFACE.asStack().getHoverName())), contraptions.size() + 1);
			return;
		}

		for (CarriageContraption contraption : contraptions)
		{
			contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
			contraption.expandBoundsAroundAxis(Axis.Y);
		}

		Train train = new Train(UUID.randomUUID(), playerUUID, graph, carriages, spacing, contraptions.stream().anyMatch(CarriageContraption::hasBackwardControls));

		if (self.lastDisassembledTrainName != null)
		{
			train.name = self.lastDisassembledTrainName;
			self.lastDisassembledTrainName = null;
		}

		for (int i = 0; i < contraptions.size(); i++)
		{
			CarriageContraption contraption = contraptions.get(i);
			Carriage carriage = carriages.get(i);
			carriage.setContraption(level, contraption);
			if (contraption.containsBlockBreakers())
				self.award(AllAdvancements.CONTRAPTION_ACTORS);

			CarriageContraptionExtension contraptionExtension = (CarriageContraptionExtension) contraption;
			CarriageExtension carriageExtension = (CarriageExtension) carriage;
			carriageExtension.getEngines().addAll(contraptionExtension.getAssembledEngines());
			carriageExtension.getHeatSources().addAll(contraptionExtension.getAssembledHeatSources());
		}

		GlobalStation station = self.getStation();
		if (station != null)
		{
			train.setCurrentStation(station);
			station.reserveFor(train);
		}

		train.collectInitiallyOccupiedSignalBlocks();
		Create.RAILWAYS.addTrain(train);
		AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(train, true));
		accessor.invokeClearException();

		self.award(AllAdvancements.TRAIN);
		if (contraptions.size() >= 6)
			self.award(AllAdvancements.LONG_TRAIN);
	}

	public static Stream<Engine> streamEngines(Train train)
	{
		return train.carriages.stream().flatMap(c -> ((CarriageExtension) c).getEngines().stream());
	}

	public static Stream<Engine> streamAliveEngines(Train train)
	{
		return streamEngines(train).filter(engine -> !engine.isOverheated());
	}

	public static Stream<HeatSource> streamHeatSources(Train train)
	{
		return train.carriages.stream().flatMap(c -> ((CarriageExtension) c).getHeatSources().stream());
	}

	public static void burnHeatSources(Train train)
	{
		if (streamAliveEngines(train).anyMatch(e -> e.getRecipe().isLimitableByHeat()))
		{
			streamHeatSources(train).forEach(h -> h.burnFuel(train));
		}

	}

	public static void tickTrainClient(Train train, Level level)
	{
		for (Engine engine : streamEngines(train).toList())
		{
			engine.tickClient(train, level);
		}

		for (HeatSource heatSource : streamHeatSources(train).toList())
		{
			heatSource.tickClient(train, level);
		}

	}

	public static void tickTrainServer(Train train, Level level)
	{
		TrainExtension extension = (TrainExtension) train;
		extension.getCoolingSystem().tick(train, level);
		streamEngines(train).forEach(e -> e.coolingAir(train));
		coolingEngines(train);

		streamEngines(train).forEach(e -> e.tickServer(train, level));
		streamHeatSources(train).forEach(h -> h.tickServer(train, level));
	}

	public static boolean coolingEngines(Train train)
	{
		List<Engine> engines = streamEngines(train).filter(e -> e.getRecipe().hasHeatCapacity()).toList();

		if (!coolingEngines(train, engines, Engine::isOverheated, Engine::getRemainHeatForAlive))
		{
			return true;
		}
		else if (!coolingEngines(train, engines, e -> true, Engine::getHeat))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 *
	 * @param train
	 * @param engines
	 * @param predicate
	 * @param toCoolFunc
	 * @return all cooled
	 */
	public static boolean coolingEngines(Train train, List<Engine> engines, Predicate<Engine> predicate, Function<Engine, Double> toCoolFunc)
	{
		while (true)
		{
			List<Engine> toCoolEngines = engines.stream().filter(predicate).toList();

			if (toCoolEngines.size() > 0)
			{
				EngineCooling cooling = coolEngines(train, toCoolEngines, toCoolFunc);

				if (cooling.toCool() == 0.0D)
				{
					return true;
				}
				else if (cooling.cooled() == 0.0D)
				{
					return false;
				}
				else
				{
					continue;
				}

			}
			else
			{
				return true;
			}

		}

	}

	public static EngineCooling coolEngines(Train train, List<Engine> engines, Function<Engine, Double> toCoolFunc)
	{
		Pair<Stream<Engine>, Double> pair = streamToCoolEngines(train, engines, toCoolFunc);
		List<Engine> toCoolEngines = pair.getFirst().toList();
		double coolingTemp = pair.getSecond();
		int toCoolEngineCount = toCoolEngines.size();

		if (toCoolEngineCount > 0 && coolingTemp > 0.0D)
		{
			double totalToCool = 0.0D;

			for (Engine engine : toCoolEngines)
			{
				totalToCool += coolingTemp * engine.getRecipe().getHeatCapacity();
			}

			double totalCooled = ((TrainExtension) train).getCoolingSystem().useCoolant(train, totalToCool);

			for (Engine engine : toCoolEngines)
			{
				double cooled = (toCoolFunc.apply(engine) / totalToCool) * totalCooled;
				engine.setHeat(engine.getHeat() - cooled);
			}

			return new EngineCooling(totalToCool, totalCooled);
		}

		return new EngineCooling(0.0D, 0.0D);
	}

	public static Pair<Stream<Engine>, Double> streamToCoolEngines(Train train, List<Engine> engines, Function<Engine, Double> func)
	{
		List<Engine> descByCoolingTemps = engines.stream().sorted(CompareUtil.desc(e -> func.apply(e) / e.getRecipe().getHeatCapacity())).toList();
		int size = descByCoolingTemps.size();

		if (size == 0)
		{
			return Pair.of(Stream.empty(), 0.0D);
		}
		else if (size == 1)
		{
			Engine engine = descByCoolingTemps.get(0);
			return Pair.of(Stream.of(engine), func.apply(engine) / engine.getRecipe().getHeatCapacity());
		}
		else
		{
			List<Engine> coolings = new ArrayList<>();
			double mostCoolingTemp = 0.0D;

			for (int i = 0; i < descByCoolingTemps.size(); i++)
			{
				Engine engine = descByCoolingTemps.get(i);
				double toCoolingTemp = func.apply(engine) / engine.getRecipe().getHeatCapacity();

				if (i == 0 || mostCoolingTemp == toCoolingTemp)
				{
					coolings.add(engine);
					mostCoolingTemp = toCoolingTemp;
				}
				else
				{
					return Pair.of(coolings.stream(), mostCoolingTemp - toCoolingTemp);
				}

			}

			Engine first = descByCoolingTemps.get(0);
			return Pair.of(coolings.stream(), func.apply(first) / first.getRecipe().getHeatCapacity());
		}

	}

	public static int getCarriagesTotalBlockCount(Train train)
	{
		int totalBlocks = 0;

		for (Carriage carriage : train.carriages)
		{
			CarriageContraptionEntity entity = carriage.anyAvailableEntity();

			if (entity != null)
			{
				totalBlocks += entity.getContraption().getBlocks().size();
			}

		}

		return totalBlocks;
	}

	private static CarriageBlocksLimit getCarriagBlockLimit(Train train, double maxBlocksPerCarriage)
	{
		return new CarriageBlocksLimit(!Double.isInfinite(maxBlocksPerCarriage), (int) (maxBlocksPerCarriage * train.carriages.size()));
	}

	public static CarriageBlocksLimit getCarriagesTotalBlockLimit(Train train, double speed, HeatState heatState)
	{
		double maxBlocksPerCarriage = 0.0D;

		for (Entry<TrainEngineTypeRecipe, EngineSpeedPlan> entry : makeEngineSpeedPlan(train, speed, heatState).entrySet())
		{
			TrainEngineTypeRecipe recipe = entry.getKey();
			int engineCount = entry.getValue().engines().size();
			FuelBurning fuel = entry.getValue().fuel();
			maxBlocksPerCarriage += fuel.isUsed() ? recipe.getMaxBlockCountPerCarriage() * engineCount : 0.0D;
		}

		return getCarriagBlockLimit(train, maxBlocksPerCarriage);
	}

	public static double getNextAccelerationSpeed(Train train, double speed, double targetSpeed, float accelerationMod)
	{
		double delta = TrainHelper.acceleration(train) * accelerationMod;
		double beforePlan = 0.0D;

		if (speed < targetSpeed)
		{
			beforePlan = Math.min(speed + delta, targetSpeed);
		}
		else
		{
			beforePlan = Math.max(speed - delta, targetSpeed);
		}

		burnHeatSources(train);

		HeatState heatState = getHeatState(train);
		double planedDelta = 0.0D;
		double maxBlocksPerCarriage = 0.0D;
		Map<TrainEngineTypeRecipe, EngineSpeedPlan> plan = makeEngineSpeedPlan(train, beforePlan * 20.0D, heatState);

		for (Entry<TrainEngineTypeRecipe, EngineSpeedPlan> entry : plan.entrySet())
		{
			TrainEngineTypeRecipe recipe = entry.getKey();
			EngineSpeedPlan enginePlan = entry.getValue();
			int engineCount = enginePlan.engines().size();
			FuelBurning fuel = enginePlan.fuel();

			planedDelta += fuel.getUsedRatio() * recipe.getAcceleration() * engineCount;
			maxBlocksPerCarriage += fuel.isUsed() ? recipe.getMaxBlockCountPerCarriage() * engineCount : 0;
		}

		planedDelta /= 400.0D;
		int carriagesTotalBlockCount = getCarriagesTotalBlockCount(train);
		CarriageBlocksLimit carriagBlockLimit = getCarriagBlockLimit(train, maxBlocksPerCarriage);

		if (carriagBlockLimit.isOvered(carriagesTotalBlockCount))
		{
			return approachTargetSpeed(train, speed, 0.0D, accelerationMod);
		}
		else if (speed < targetSpeed)
		{
			return Math.min(speed + planedDelta, targetSpeed);
		}
		else
		{
			return Math.max(speed - planedDelta, targetSpeed);
		}

	}

	public static double approachTargetSpeed(Train train, double speed, double targetSpeed, float accelerationMod)
	{
		if (Mth.equal(speed, targetSpeed))
		{
			return speed;
		}

		double delta = getSpeedDelta(train, speed, targetSpeed) * accelerationMod;

		if (speed < targetSpeed)
		{
			return Math.min(speed + delta, targetSpeed);
		}
		else if (speed > targetSpeed)
		{
			return Math.max(speed - delta, targetSpeed);
		}
		else
		{
			return speed;
		}

	}

	public static float getSpeedDelta(Train train, double speed, double targetSpeed)
	{
		if (speed < targetSpeed)
		{
			return speed >= 0 ? TrainHelper.acceleration(train) : TrainHelper.deacceleration(train);
		}
		else
		{
			return speed <= 0 ? TrainHelper.acceleration(train) : TrainHelper.deacceleration(train);
		}

	}

	public static void controlSpeed(Train train)
	{
		TrainExtension trainE = ((TrainExtension) train);
		float acelerationMod = 0.0F;
		double speed = train.speed;

		float approachAccelerationMod = trainE.getApproachAccelerationMod();
		boolean fuelFuelBurned = false;

		if (approachAccelerationMod != 0.0F)
		{
			acelerationMod = approachAccelerationMod;
			trainE.setApproachAccelerationMod(0.0F);

			if (((speed < train.targetSpeed) && speed >= 0) || ((speed > train.targetSpeed) && speed <= 0))
			{
				speed = getNextAccelerationSpeed(train, speed, train.targetSpeed, approachAccelerationMod);
				fuelFuelBurned = true;
			}
			else
			{
				speed = approachTargetSpeed(train, speed, train.targetSpeed, approachAccelerationMod);
			}

		}
		else
		{
			acelerationMod = 1.0F;
		}

		if (!Mth.equal(speed, 0.0D))
		{
			if (!fuelFuelBurned)
			{
				burnHeatSources(train);
			}

			HeatState heatState = getHeatState(train);
			Map<TrainEngineTypeRecipe, EngineSpeedPlan> plan = makeEngineSpeedPlan(train, speed * 20.0D, heatState);
			double reductionRatio = getCarriagesSpeedReductionRatio(train);
			double targetSpeed = 0.0D;

			for (Entry<TrainEngineTypeRecipe, EngineSpeedPlan> pair : plan.entrySet())
			{
				TrainEngineTypeRecipe recipe = pair.getKey();
				List<Engine> engines = pair.getValue().engines();
				double eachSpeed = pair.getValue().eachSpeed();
				int engineCount = engines.size();
				FuelBurning fuel = burnFuel(train, recipe, engineCount, eachSpeed, heatState.level(), false);

				for (Engine engine : engines)
				{
					engine.onFuelBurned(fuel, eachSpeed, heatState.level());
					targetSpeed += engine.getSpeed() * reductionRatio;
				}

			}

			if (speed < 0)
			{
				targetSpeed = -targetSpeed;
			}

			train.speed = approachTargetSpeed(train, train.speed, targetSpeed / 20.0D, acelerationMod);
		}
		else
		{
			train.speed = 0.0D;
			streamEngines(train).forEach(e -> e.setSpeed(0.0D));
		}

		// when fuel not enough during test
		// train.speed = approachTargetSpeed(train, train.speed, train.targetSpeed, acelerationMod);

		if (train.manualTick & !Mth.equal(train.speed, 0.0D))
		{
			train.leaveStation();
		}

	}

	public static Map<TrainEngineTypeRecipe, EngineSpeedPlan> makeEngineSpeedPlan(Train train, double speed, HeatState heatState)
	{
		double maxSpeed = maxSpeed(train) * 20.0D;
		double speedRatio = Math.abs(speed) / maxSpeed;
		Map<TrainEngineTypeRecipe, Double> speedPlans = new HashMap<>();
		Map<TrainEngineTypeRecipe, EngineSpeedPlan> speedPredict = new HashMap<>();
		Map<TrainEngineTypeRecipe, List<Engine>> engineMap = streamAliveEngines(train).collect(Collectors.groupingBy(Engine::getRecipe));

		for (Entry<TrainEngineTypeRecipe, List<Engine>> entry : engineMap.entrySet())
		{
			TrainEngineTypeRecipe recipe = entry.getKey();
			List<Engine> engines = entry.getValue();
			speedPlans.put(recipe, speedRatio * recipe.getMaxSpeed());
			speedPredict.put(recipe, new EngineSpeedPlan(engines, new FuelBurning(false, 0.0D, 0.0D), 0.0D));
		}

		double heatSpeedLimit = heatState.speedLimit();

		while (true)
		{
			double totalNeeded = 0.0D;
			List<TrainEngineTypeRecipe> enoughs = new ArrayList<>();
			List<TrainEngineTypeRecipe> neededs = new ArrayList<>();

			for (TrainEngineTypeRecipe recipe : speedPlans.keySet())
			{
				List<Engine> engines = engineMap.get(recipe);
				int engineCount = engines.size();
				double eachSpeedPlan = speedPlans.get(recipe);
				double sumSpeedPlan = eachSpeedPlan * engineCount;
				double eachLimitedPlan = eachSpeedPlan;
				double heatSpeedOver = sumSpeedPlan - heatSpeedLimit;

				if (recipe.isLimitableByHeat() && heatSpeedOver > 0.0D)
				{
					sumSpeedPlan -= heatSpeedOver;
					eachLimitedPlan = sumSpeedPlan / engineCount;
				}

				FuelBurning fuel = burnFuel(train, recipe, engineCount, eachLimitedPlan, heatState.level(), true);
				double eachPredict = recipe.getPredictSpeed(fuel.toBurn(), fuel.burned(), eachLimitedPlan, heatState.level());
				speedPredict.put(recipe, new EngineSpeedPlan(engines, fuel, eachPredict));

				if (Mth.equal(eachPredict, eachSpeedPlan) || eachPredict >= eachSpeedPlan)
				{
					enoughs.add(recipe);
				}
				else
				{
					neededs.add(recipe);
					totalNeeded += (eachSpeedPlan - eachPredict) * engineCount;

					if (recipe.isLimitableByHeat())
					{
						heatSpeedLimit = Math.max(heatSpeedLimit - heatSpeedOver, 0.0D);
					}

				}

			}

			if (Mth.equal(totalNeeded, 0.0D) || enoughs.size() == 0)
			{
				break;
			}

			for (TrainEngineTypeRecipe recipe : neededs)
			{
				speedPlans.remove(recipe);
			}

			double planAdding = totalNeeded / enoughs.size();

			for (TrainEngineTypeRecipe recipe : enoughs)
			{
				double prevPlan = speedPlans.get(recipe);
				double nextPlan = prevPlan + planAdding / engineMap.get(recipe).size();
				speedPlans.put(recipe, nextPlan);
			}

		}

		return speedPredict;
	}

	public static FuelBurning burnFuel(Train train, TrainEngineTypeRecipe recipe, int engineCount, double speed, int heatLevel, boolean simulate)
	{
		double totalToBurn = recipe.getFuelUsage(speed, heatLevel) / 20.0D * engineCount;
		double eachToBurn = totalToBurn / engineCount;

		if (speed > 0.0D)
		{
			List<FluidTagEntry> fuelType = recipe.getFuelTypes();
			double totalBurned = ((TrainExtension) train).getFuelBurner().burn(train, fuelType, totalToBurn, simulate);
			double eachBurned = totalBurned / engineCount;
			return new FuelBurning(true, eachToBurn, eachBurned);
		}
		else
		{
			return new FuelBurning(false, eachToBurn, 0.0D);
		}

	}

	public static double getCarriagesSpeedReductionRatio(Train train)
	{
		double heap = 0.0D;

		for (Engine engine : streamAliveEngines(train).toList())
		{
			heap += engine.getRecipe().getCarriageSpeedStressHeap();
		}

		return Mth.clamp(1.0D - (TrainEngineTypeRecipe.getCarriageStress(train.carriages.size()) / heap), 0.0D, 1.0D);
	}

	public static float maxSpeedBeforeReduction(Train train)
	{
		Double collect = streamAliveEngines(train).collect(Collectors.summingDouble(v -> v.getRecipe().getMaxSpeed()));
		return (collect != null ? collect.floatValue() : AllConfigs.server().trains.trainTopSpeed.getF()) / 20;
	}

	public static float maxSpeed(Train train)
	{
		float original = maxSpeedBeforeReduction(train);
		return (float) Math.max(original * getCarriagesSpeedReductionRatio(train), 0.0F);
	}

	public static float maxTurnSpeed(Train train)
	{
		return maxSpeed(train) * 0.5F;
	}

	public static float acceleration(Train train)
	{
		Double collect = streamAliveEngines(train).collect(Collectors.summingDouble(v -> v.getRecipe().getAcceleration()));
		return (collect != null ? (float) (collect.doubleValue() * getCarriagesSpeedReductionRatio(train)) : AllConfigs.server().trains.trainAcceleration.getF()) / 400;
	}

	public static float deacceleration(Train train)
	{
		Double collect = streamEngines(train).collect(Collectors.summingDouble(v -> v.getRecipe().getAcceleration()));

		if (collect != null)
		{
			return Math.max(collect.floatValue(), AllConfigs.server().trains.trainAcceleration.getF()) / 400;
		}
		else
		{
			return AllConfigs.server().trains.trainAcceleration.getF() / 400;
		}

	}

	public static void addToGoggleTooltip(Train train, List<Component> tooltip, boolean isPlayerSneaking, CarriageContraptionEntity carriageContraptionEntity, BlockHitResult carriageContraptionRayTraceResult)
	{
		List<Engine> engines = streamEngines(train).toList();

		if (engines.size() == 1)
		{
			Engine engine = engines.get(0);

			if (engine.isOverheated())
			{
				TrainEngineTypeRecipe recipe = engine.getRecipe();
				int heatCapacity = recipe.getHeatCapacity();
				double temp = engine.getHeat() / heatCapacity;
				double airCoolingDuration = ((temp - recipe.getOverheatedResettingTemp()) * heatCapacity) / recipe.getAirCoolingRate();
				Lang.builder().add(Component.translatable(TRAIN_GOGGLE_OVERHEATED)).style(ChatFormatting.GOLD).style(ChatFormatting.BOLD).forGoggles(tooltip);
				Lang.builder().add(Component.translatable(TRAIN_GOGGLE_OVERHEATED_1)).style(ChatFormatting.GRAY).forGoggles(tooltip);
				Lang.builder().add(Component.translatable(TRAIN_GOGGLE_OVERHEATED_2, Component.literal(NumberHelper.format(airCoolingDuration, 1)).withStyle(ChatFormatting.WHITE))).style(ChatFormatting.GRAY).forGoggles(tooltip);
				return;
			}

		}

		HeatState heatState = getHeatState(train);
		double speed = ((CarriageSyncDataExtension) carriageContraptionEntity.getCarriageData()).getTrainSpeed() * 20;
		int carriagesTotalBlockCount = getCarriagesTotalBlockCount(train);
		CarriageBlocksLimit carriagesTotalBlockLimit = getCarriagesTotalBlockLimit(train, Math.abs(speed) + acceleration(train), heatState);
		MutableComponent speedComponent = Component.literal(NumberHelper.format(speed, 1) + "m/s").withStyle(ChatFormatting.GOLD);
		MutableComponent maxSpeedComponent = Component.literal(NumberHelper.format(train.maxSpeed() * 20, 1) + "m/s").withStyle(ChatFormatting.DARK_GRAY);
		MutableComponent blocksComponent = Component.literal(NumberHelper.format(carriagesTotalBlockCount)).withStyle(carriagesTotalBlockLimit.isOvered(carriagesTotalBlockCount) ? ChatFormatting.RED : ChatFormatting.GOLD);
		MutableComponent blocksLimitComponent = Component.literal(carriagesTotalBlockLimit.hasLimit() ? NumberHelper.format(carriagesTotalBlockLimit.limitBlocks()) : "∞").withStyle(ChatFormatting.DARK_GRAY);

		Lang.builder().add(Component.translatable(TRAIN_GOGGLE_TRAIN_INFO)).forGoggles(tooltip);
		Lang.builder().add(Component.translatable(TRAIN_GOGGLE_TRAIN_SPEED, speedComponent, maxSpeedComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
		Lang.builder().add(Component.translatable(TRAIN_GOGGLE_TRAIN_BLOCKS, blocksComponent, blocksLimitComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);

		if (heatState.sources() > 0)
		{
			MutableComponent heatLevelComponent = Component.literal(NumberHelper.format(heatState.level())).withStyle(ChatFormatting.GOLD);
			MutableComponent maxHeatLevelComponent = Component.literal(NumberHelper.format(heatState.maxLevel())).withStyle(ChatFormatting.DARK_GRAY);
			Lang.builder().add(Component.translatable(TRAIN_GOGGLE_HEAT_SOURCE_INFO)).forGoggles(tooltip);
			Lang.builder().add(Component.translatable(TRAIN_GOGGLE_HEAT_SOURCE_LEVEL, heatLevelComponent, maxHeatLevelComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
		}

		Lang.builder().add(Component.translatable(TRAIN_GOGGLE_ENGINE_INFO)).forGoggles(tooltip);

		if (engines.size() == 1)
		{
			for (Engine engine : engines)
			{
				TrainEngineTypeRecipe recipe = engine.getRecipe();
				int heatCapacity = recipe.getHeatCapacity();
				Lang.builder().add(engine.getItem().getHoverName().copy()).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);

				if (heatCapacity > 0)
				{
					double temp = engine.getHeat() / heatCapacity;
					Lang.builder().add(Component.translatable(TRAIN_GOGGLE_ENGINE_TEMP, getHeatPercentComponent(temp))).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
				}

			}

		}
		else
		{

			for (Entry<TrainEngineTypeRecipe, List<Engine>> entry : engines.stream().collect(Collectors.groupingBy(Engine::getRecipe)).entrySet())
			{
				TrainEngineTypeRecipe recipe = entry.getKey();
				Component name = null;

				for (Engine engine : entry.getValue())
				{
					name = engine.getItem().getHoverName();
					break;
				}

				MutableComponent countComponent = Component.literal(NumberHelper.format(entry.getValue().size()));
				Lang.builder().add(Component.translatable(TRAIN_GOGGLE_ENGINE_COUNT, name != null ? name.copy() : Component.empty(), countComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);

				int heatCapacity = recipe.getHeatCapacity();

				if (heatCapacity > 0)
				{
					int overheates = 0;
					double hostHeat = 0.0D;

					for (Engine engine : entry.getValue())
					{
						if (engine.isOverheated())
						{
							overheates++;
						}

						hostHeat = Math.max(engine.getHeat(), hostHeat);
					}

					double highestTemp = hostHeat / heatCapacity;
					MutableComponent highestTempComponent = getHeatPercentComponent(highestTemp);
					MutableComponent overheatedsComponent = Component.literal(NumberHelper.format(overheates)).withStyle(overheates > 0 ? ChatFormatting.RED : ChatFormatting.GRAY);
					Lang.builder().add(Component.translatable(TRAIN_GOGGLE_ENGINE_HIGHEST_TEMP, highestTempComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 2);
					Lang.builder().add(Component.translatable(TRAIN_GOGGLE_ENGINE_OVERHEATEDS, overheatedsComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 2);
				}

			}

		}

		Map<FluidStack, Long> amountMap = new HashMap<>();
		long totalCapacity = 0L;
		boolean hasTank = false;

		for (Carriage carriage : train.carriages)
		{
			IFluidHandler fluids = ((MountedStorageManagerExtension) carriage.storage).getSyncedFluids();

			for (int tank = 0; tank < fluids.getTanks(); tank++)
			{
				hasTank = true;
				totalCapacity += fluids.getTankCapacity(tank);
				FluidStack fluid = fluids.getFluidInTank(tank);

				if (fluid.isEmpty())
				{
					continue;
				}

				FluidStack type = FluidHelper.deriveAmount(fluid, 1);
				long amount = amountMap.computeIfAbsent(type, t -> 0L);
				amount += fluid.getAmount();
				amountMap.put(type, amount);
			}

		}

		if (hasTank)
		{
			Lang.translate("gui.goggles.fluid_container").forGoggles(tooltip);
			Lang.builder().add(Component.translatable(TRAIN_GOGGLE_FLUID_CAPACITY, Component.literal(NumberHelper.format(totalCapacity) + "mB"))).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);

			for (Entry<FluidStack, Long> entry : amountMap.entrySet())
			{
				Component displayName = entry.getKey().getDisplayName();
				Component percentComponent = Component.literal(NumberHelper.format(entry.getValue() / (totalCapacity / 100.0D), 1) + "%").withStyle(ChatFormatting.GOLD);
				Component amountComponent = Component.literal(NumberHelper.format(entry.getValue()) + "mB").withStyle(ChatFormatting.GOLD);
				Lang.builder().add(Component.translatable(TRAIN_GOGGLE_FLUID_AMOUNT, displayName, percentComponent, amountComponent)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
			}

		}

	}

	public static MutableComponent getHeatPercentComponent(double temp)
	{
		return Component.literal(NumberHelper.format(temp * 100.0D, 1) + "%").setStyle(Style.EMPTY.withColor(Mth.hsvToRgb((float) ((1.0D - temp) / 3.0F), (float) (temp), 1.0F)));
	}

	public static void onCrash(Train train)
	{
		streamEngines(train).forEach(e ->
		{
			CrashBehavior crashBehavior = e.getRecipe().getCrashBehavior();

			if (crashBehavior.getExplosionRadius() > 0.0F)
			{
				for (Carriage carriage : train.carriages)
				{
					CarriageContraptionEntity contraptionEntity = carriage.anyAvailableEntity();

					if (contraptionEntity != null)
					{
						Vec3 pos = contraptionEntity.position();
						contraptionEntity.getLevel().explode(contraptionEntity, pos.x(), pos.y(), pos.z(), crashBehavior.getExplosionRadius(), crashBehavior.isCausesFire(), crashBehavior.getExplosionMode());
					}

				}

			}

		});

	}

	public static boolean anyEngineHasHeat(Train train)
	{
		return streamEngines(train).anyMatch(e -> e.getHeat() > 0.0D);
	}

	public static HeatState getHeatState(Train train)
	{
		List<HeatSource> heatSources = streamHeatSources(train).toList();

		if (heatSources.size() > 0)
		{
			int level = 0;
			int maxLevel = 0;
			float speedLimit = 0.0F;
			float maxSpeedLimit = 0.0F;

			for (HeatSource source : heatSources)
			{
				level += source.getLevel();
				maxLevel += source.getMaxLevel();
				speedLimit += HeatStage.getSpeedLimit(source.getLevel());
				maxSpeedLimit += HeatStage.getSpeedLimit(source.getMaxLevel());
			}

			return new HeatState(level, speedLimit, heatSources.size(), maxLevel, maxSpeedLimit);
		}
		else
		{
			return new HeatState(0, 0.0F, 0, 0, 0.0F);
		}

	}

	public static <PART extends TrainPart<?>> List<PART> readTrainParts(FriendlyByteBuf buffer, Function<CompoundTag, PART> factory)
	{
		return buffer.readList(b -> factory.apply(b.readNbt()));
	}

	public static <PART extends TrainPart<?>> void writeTrainParts(FriendlyByteBuf buffer, Collection<PART> parts)
	{
		buffer.writeCollection(parts, (b, p) -> b.writeNbt(p.toNbt()));
	}

	public static <PART extends TrainPart<?>> PART getPart(Carriage carriage, Function<CarriageExtension, List<? extends PART>> func, BlockPos localPos)
	{
		for (PART part : func.apply(((CarriageExtension) carriage)))
		{
			if (part.getBlockPos().equals(localPos))
			{
				return part;
			}

		}

		return null;
	}

	public static Engine getEngine(Carriage carriage, BlockPos localPos)
	{
		return getPart(carriage, CarriageExtension::getEngines, localPos);
	}

	public static HeatSource getHeatSource(Carriage carriage, BlockPos localPos)
	{
		return getPart(carriage, CarriageExtension::getHeatSources, localPos);
	}

	private TrainHelper()
	{

	}

}
