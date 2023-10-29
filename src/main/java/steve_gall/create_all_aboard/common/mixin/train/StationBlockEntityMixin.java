package steve_gall.create_all_aboard.common.mixin.train;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.station.StationBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_all_aboard.common.content.train.TrainHelper;

@Mixin(value = StationBlockEntity.class, remap = false)
public abstract class StationBlockEntityMixin extends BlockEntity
{
	public StationBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}

	@Inject(method = "assemble", at = @At(value = "HEAD"), cancellable = true)
	private void assemble(UUID playerUUID, CallbackInfo ci)
	{
		TrainHelper.assemble((StationBlockEntity) (Object) this, playerUUID);
		ci.cancel();
	}

}
