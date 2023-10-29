package steve_gall.create_all_aboard.common.content.train;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_all_aboard.common.crafting.TrainEngineTypeRecipe;

public record HeatSourcePos(BlockPos localPos, BlockState blockState, ItemStack item, TrainEngineTypeRecipe recipe)
{

}
