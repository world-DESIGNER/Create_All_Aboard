package steve_gall.create_trainwrecked.common.content.train;

import java.util.Collection;
import java.util.List;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.config.ConfigBase.ConfigFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.content.fluid.tank.FuelTankHelper;

public class CarriageContraptionHelper
{
	public static void capture(CarriageContraption carriageContraption, Level level, BlockPos pos)
	{
		CarriageContraptionExtension extension = (CarriageContraptionExtension) carriageContraption;
		captureParts(extension, level, pos);

		BlockEntity blockEntity = level.getBlockEntity(pos);

		if (blockEntity instanceof FluidTankBlockEntity tank)
		{
			FuelTankHelper.resetBoiler(tank);
		}

	}

	public static void captureParts(CarriageContraptionExtension contraption, Level level, BlockPos pos)
	{
		BlockPos localPos = contraption.toLocalPos(pos);
		BlockState blockState = level.getBlockState(pos);
		ItemStack item = new ItemStack(blockState.getBlock());
		CapturedPos capturedPos = new CapturedPos(localPos, blockState, item);

		captureParts(contraption, level, pos, capturedPos);
	}

	private static void captureParts(CarriageContraptionExtension contraption, Level level, BlockPos worldPos, CapturedPos captured)
	{
		registerPart(contraption.getAssembledEngines(), new Engine(level, captured));

		if (level.getBlockState(worldPos.offset(0, 1, 0)).getBlock() instanceof FluidTankBlock)
		{
			registerPart(contraption.getAssembledHeatSources(), new HeatSource(level, captured));
		}

	}

	private static <PART extends TrainPart<?>> void registerPart(List<PART> list, PART part)
	{
		if (part.isRecipeFound())
		{
			list.add(part);
		}

	}

	public static void control(CarriageContraptionEntity carriageContraption, BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player)
	{
		ConfigFloat config = AllConfigs.server().trains.manualTrainSpeedModifier;

		if (config.get() != 1.0D)
		{
			config.set(1.0D);
		}

	}

	private CarriageContraptionHelper()
	{

	}

}
