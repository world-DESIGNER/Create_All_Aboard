package steve_gall.create_trainwrecked.common.content.train;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraption;
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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import steve_gall.create_trainwrecked.common.CreateTrainwrecked;
import steve_gall.create_trainwrecked.common.config.CreateTrainwreckedConfig;
import steve_gall.create_trainwrecked.common.mixin.StationBlockEntityAccessor;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public class TrainHelper
{
	public static String NO_ENGINES = CreateTrainwrecked.translationKey("train_assembly.no_engines");
	public static String TOO_MANY_CARRIAGES = CreateTrainwrecked.translationKey("train_assembly.too_many_carriages");
	public static String TOO_MANY_BLOCKS = CreateTrainwrecked.translationKey("train_assembly.too_many_blocks");

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
		boolean atLeastOneForwardControls = false;
		boolean atLeastOneEngines = false;
		double totalEngineCarriageStressHeap = 0.0D;
		double maxBlocksPerCarriage = 0.0D;
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
				for (EnginPos enginePos : ((CarriageContraptionExtension) contraption).getAssembledEnginePos())
				{
					atLeastOneEngines = true;
					totalEngineCarriageStressHeap += enginePos.recipe().getCarriageSpeedStressHeap();
					maxBlocksPerCarriage += enginePos.recipe().getMaxBlockCountPerCarriage();
				}
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

			totalBlocks += contraption.getBlocks().size();
			contraptions.add(contraption);
			carriages.add(new Carriage(firstBogey, secondBogey, bogeySpacing));
		}

		if (!atLeastOneForwardControls)
		{
			accessor.invokeException(new AssemblyException(Lang.translateDirect("train_assembly.no_controls")), -1);
			return;
		}

		if (!atLeastOneEngines)
		{
			accessor.invokeException(new AssemblyException(new TranslatableComponent(NO_ENGINES)), -1);
			return;
		}

		int maxCarriageCount = (int) TrainEngineRecipe.getMaxCarriageCount(totalEngineCarriageStressHeap);

		if (!Double.isInfinite(totalEngineCarriageStressHeap) && maxCarriageCount < carriages.size())
		{
			accessor.invokeException(new AssemblyException(new TranslatableComponent(TOO_MANY_CARRIAGES, maxCarriageCount, carriages.size())), -1);
			return;
		}

		int maxBlocks = (int) (maxBlocksPerCarriage * carriages.size());

		if (!Double.isInfinite(maxBlocksPerCarriage) && maxBlocks < totalBlocks)
		{
			accessor.invokeException(new AssemblyException(new TranslatableComponent(TOO_MANY_BLOCKS, maxBlocks, totalBlocks)), -1);
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

			List<EnginPos> assembledEnginePos = ((CarriageContraptionExtension) contraption).getAssembledEnginePos();
			((CarriageExtension) carriage).getEngines().addAll(assembledEnginePos.stream().map(Engine::new).toList());
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

	public static void tickTrain(Train train, Level level)
	{
		for (Engine engine : streamEngines(train).toList())
		{
			engine.tick(train, level);
		}

		System.out.println("targetSpeed: " + String.format("%.3f", train.targetSpeed * 20) + " b/s, speed: " + String.format("%.3f", train.speed * 20) + " b/s");
	}

	public static double getPredictSpeed(Train train, double speed, double targetSpeed, float accelerationMod)
	{
		if (Mth.equal(speed, targetSpeed))
		{
			return speed;
		}

		double delta = getSpeedDelta(train, speed, targetSpeed) * accelerationMod;

		if (speed < targetSpeed)
		{
			speed = Math.min(speed + delta, targetSpeed);
		}
		else if (speed > targetSpeed)
		{
			speed = Math.max(speed - delta, targetSpeed);
		}

		return speed;
	}

	public static void applyFuelSpeed(Train train)
	{
		TrainExtension trainE = ((TrainExtension) train);
		float acelerationMod = 0.0F;
		double speed = train.speed;

		{
			float approachAccelerationMod = trainE.getApproachAccelerationMod();

			if (approachAccelerationMod != 0.0F)
			{
				acelerationMod = approachAccelerationMod;
				trainE.setApproachAccelerationMod(0.0F);
				speed = getPredictSpeed(train, speed, train.targetSpeed, approachAccelerationMod);
			}
			else
			{
				acelerationMod = 1.0F;
			}

		}

		Map<TrainEngineRecipe, List<Engine>> engineMap = streamEngines(train).collect(Collectors.groupingBy(e -> e.getRecipe()));

		if (engineMap.size() > 0 && !Mth.equal(speed, 0.0D))
		{
			double maxSpeed = train.maxSpeed();
			double targetSpeed = train.targetSpeed;
			double reductionRatio = getCarriageSpeedReductionRatio(train);
			double targetSpeedRatio = Math.abs(targetSpeed) / maxSpeed;
			double absNextSpeed = 0.0D;

			for (Entry<TrainEngineRecipe, List<Engine>> entry : engineMap.entrySet())
			{
				TrainEngineRecipe recipe = entry.getKey();
				List<Engine> engines = entry.getValue();
				int duplicatedRecipeCount = engines.size() - 1;
				double allocatedSpeed = targetSpeedRatio * recipe.getMaxSpeed();
				double toBurn = recipe.getFuelUsage(duplicatedRecipeCount, allocatedSpeed) / 20.0D;

				if (recipe.isFuelShare())
				{
					double sharedBurned = trainE.getFuelBurner().burn(train, recipe.getFuelType(), toBurn);
					double eachBurned = sharedBurned / engines.size();

					for (Engine engine : engines)
					{
						engine.onFuelBurned(eachBurned, allocatedSpeed);
					}

				}
				else
				{
					for (Engine engine : engines)
					{
						double burned = engine.getFuelBurner().burn(train, recipe.getFuelType(), toBurn);
						engine.onFuelBurned(burned, allocatedSpeed);
					}

				}

				for (Engine engine : engines)
				{
					absNextSpeed += engine.getSpeed() * reductionRatio;
				}

			}

			if (targetSpeed < 0)
			{
				absNextSpeed = -absNextSpeed;
			}

			train.speed = getPredictSpeed(train, train.speed, absNextSpeed, acelerationMod);
		}

		// when fuel not enough during test
		// train.speed = getPredictSpeed(train, train.speed, train.targetSpeed, acelerationMod);;

		if (train.manualTick & !Mth.equal(train.speed, 0.0D))
		{
			train.leaveStation();
		}

	}

	public static double getCarriageSpeedReductionRatio(Train train)
	{
		double heap = 0.0D;

		for (Engine engine : streamEngines(train).toList())
		{
			heap += engine.getRecipe().getCarriageSpeedStressHeap();
		}

		return 1.0D - (getCarriageStress(train.carriages.size()) / heap);
	}

	public static float getCarriageStress(int carriageCount)
	{
		if (carriageCount <= 1)
		{
			return 0.0F;
		}
		else
		{
			return (carriageCount - 1) * CreateTrainwreckedConfig.COMMON.carriageSpeedStress.get();
		}

	}

	public static float maxSpeedBeforeReduction(Train train)
	{
		Double collect = streamEngines(train).collect(Collectors.summingDouble(v -> v.getRecipe().getMaxSpeed()));
		return (collect != null ? collect.floatValue() : AllConfigs.server().trains.trainTopSpeed.getF()) / 20;
	}

	public static float maxSpeed(Train train)
	{
		float original = maxSpeedBeforeReduction(train);
		return (float) Math.max(original * getCarriageSpeedReductionRatio(train), 0.0F);
	}

	public static float maxTurnSpeed(Train train)
	{
		return maxSpeed(train) * 0.5F;
	}

	public static float acceleration(Train train)
	{
		Double collect = streamEngines(train).collect(Collectors.summingDouble(v -> v.getRecipe().getAcceleration()));
		return (collect != null ? collect.floatValue() : AllConfigs.server().trains.trainAcceleration.getF()) / 400;
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

	private TrainHelper()
	{

	}

}
