package steve_gall.create_all_aboard.common.content.train;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public record CapturedPos(BlockPos localPos, BlockState blockState, ItemStack item)
{

}
