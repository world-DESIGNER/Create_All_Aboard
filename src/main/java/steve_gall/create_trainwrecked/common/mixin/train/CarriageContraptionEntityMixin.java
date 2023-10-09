package steve_gall.create_trainwrecked.common.mixin.train;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import steve_gall.create_trainwrecked.common.content.train.CarriageContraptionHelper;

@Mixin(value = CarriageContraptionEntity.class)
public abstract class CarriageContraptionEntityMixin extends OrientedContraptionEntity
{
	public CarriageContraptionEntityMixin(EntityType<?> type, Level world)
	{
		super(type, world);
	}

	@Inject(method = "control", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private void control(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player, CallbackInfoReturnable<Boolean> cir)
	{
		CarriageContraptionHelper.control((CarriageContraptionEntity) (Object) this, controlsLocalPos, heldControls, player);
	}

}
