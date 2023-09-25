package steve_gall.create_trainwrecked.common.content.train;

import java.util.Collection;
import java.util.List;

import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.config.ConfigBase.ConfigFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.crafting.TrainEngineTypeRecipe;
import steve_gall.create_trainwrecked.common.init.ModRecipeTypes;
import steve_gall.create_trainwrecked.common.util.ItemTagEntry;

public class CarriageContraptionHelper
{
	public static void capture(CarriageContraption carriageContraption, Level level, BlockPos pos)
	{
		CarriageContraptionExtension extension = (CarriageContraptionExtension) carriageContraption;
		BlockState blockState = level.getBlockState(pos);
		ItemStack item = new ItemStack(blockState.getBlock());
		List<TrainEngineTypeRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRAIN_ENGINE_TYPE.get());
		TrainEngineTypeRecipe recipe = recipes.stream().filter(r -> ItemTagEntry.TYPE.testIngredient(r.getBlocks(), item)).findFirst().orElse(null);

		if (recipe != null)
		{
			extension.getAssembledEnginePos().add(new EnginPos(extension.toLocalPos(pos), blockState, item, recipe));
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
