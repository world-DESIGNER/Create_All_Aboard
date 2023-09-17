package steve_gall.create_trainwrecked.common.content.train;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.recipe.TrainEngineRecipe;

public record EnginPos(BlockPos localPos, BlockState blockState, ItemStack item, TrainEngineRecipe recipe)
{

}