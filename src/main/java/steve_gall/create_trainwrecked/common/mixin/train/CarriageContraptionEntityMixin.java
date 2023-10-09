package steve_gall.create_trainwrecked.common.mixin.train;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.config.ConfigBase.ConfigFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
		ConfigFloat config = AllConfigs.server().trains.manualTrainSpeedModifier;

		if (config.get() != 1.0D)
		{
			config.set(1.0D);
		}

	}

}
