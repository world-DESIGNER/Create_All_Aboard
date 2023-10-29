package steve_gall.create_all_aboard.client.mixin.train;

import java.lang.ref.WeakReference;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.ContraptionHandlerClient;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.utility.Couple;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import steve_gall.create_all_aboard.common.content.train.TrainHelper;

@Mixin(value = TrainRelocator.class, remap = false)
public abstract class TrainRelocatorMixin
{
	@Shadow
	private static WeakReference<CarriageContraptionEntity> hoveredEntity;

	@Shadow
	private static Train getTrainFromEntity(CarriageContraptionEntity carriageContraptionEntity)
	{
		return null;
	}

	@Inject(method = "addToTooltip", at = @At(value = "TAIL"), cancellable = true)
	private static void addToTooltip(List<Component> tooltip, boolean shiftKeyDown, CallbackInfoReturnable<Boolean> cir)
	{
		if (cir.getReturnValueZ())
		{
			return;
		}

		CarriageContraptionEntity carriageContraptionEntity = hoveredEntity.get();
		Train train = getTrainFromEntity(carriageContraptionEntity);

		if (train != null)
		{
			Minecraft mc = Minecraft.getInstance();
			Couple<Vec3> rayInputs = ContraptionHandlerClient.getRayInputs(mc.player);
			Vec3 origin = rayInputs.getFirst();
			Vec3 target = rayInputs.getSecond();
			BlockHitResult rayTraceResult = ContraptionHandlerClient.rayTraceContraption(origin, target, carriageContraptionEntity);

			if (rayTraceResult != null)
			{
				TrainHelper.addToGoggleTooltip(train, tooltip, shiftKeyDown, carriageContraptionEntity, rayTraceResult);
				cir.setReturnValue(true);
			}

		}

	}

}
