package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.trains.entity.CarriageContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

		if (blockState.isAir())
		{
			return;
		}

		ItemStack item = new ItemStack(blockState.getBlock());

		if (item.isEmpty())
		{
			return;
		}

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

	public static void copyData(List<? extends TrainPart<?>> to, List<CompoundTag> from)
	{
		int size = Math.min(to.size(), from.size());

		for (int i = 0; i < size; i++)
		{
			to.get(i).readSyncData(from.get(i));
		}

	}

	private CarriageContraptionHelper()
	{

	}

}
