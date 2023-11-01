package steve_gall.create_all_aboard.common.mixin.engine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_all_aboard.common.block.TrainSteamEngineBlock;

@Mixin(value = PoweredShaftBlock.class)
public abstract class PoweredShaftBlockMixin
{
	@Inject(method = "stillValid", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void stillValid(BlockState pState, LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir)
	{
		for (Direction d : Iterate.directions)
		{
			if (d.getAxis() == pState.getValue(PoweredShaftBlock.AXIS))
				continue;
			BlockPos enginePos = pPos.relative(d, 2);
			BlockState engineState = pLevel.getBlockState(enginePos);
			if (!(engineState.getBlock() instanceof TrainSteamEngineBlock engine))
				continue;
			if (!SteamEngineBlock.getShaftPos(engineState, enginePos).equals(pPos))
				continue;
			if (SteamEngineBlock.isShaftValid(engineState, pState))
				cir.setReturnValue(true);
		}

	}

}
