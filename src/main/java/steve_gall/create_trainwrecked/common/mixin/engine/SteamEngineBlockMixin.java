package steve_gall.create_trainwrecked.common.mixin.engine;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = SteamEngineBlock.class)
public abstract class SteamEngineBlockMixin extends FaceAttachedHorizontalDirectionalBlock
{
	public SteamEngineBlockMixin(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos)
	{
		return super.canSurvive(pState, pLevel, pPos);
	}

}
