package steve_gall.create_trainwrecked.common.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.contraptions.MountedStorageManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import steve_gall.create_trainwrecked.common.content.contraption.MountedStorageManagerExtension;
import steve_gall.create_trainwrecked.common.item.JerrycanItem;

@Mixin(value = MountedStorageManager.class, remap = false)
public abstract class MountedStorageManagerMixin implements MountedStorageManagerExtension
{
	@Shadow
	private Map<BlockPos, MountedFluidStorage> fluidStorage;

	@Inject(method = "handlePlayerStorageInteraction", at = @At(value = "HEAD"), cancellable = true)
	private void handlePlayerStorageInteraction(Contraption contraption, Player player, BlockPos localPos, CallbackInfoReturnable<Boolean> cir)
	{
		InteractionHand hand = InteractionHand.MAIN_HAND;

		if (player.getItemInHand(hand).getItem() instanceof JerrycanItem item)
		{
			if (item.onUse(player, hand, contraption, localPos))
			{
				cir.setReturnValue(true);
			}

		}

	}

	@Override
	@Unique
	public MountedFluidStorage getfluidStorage(BlockPos localPos)
	{
		return this.fluidStorage.get(localPos);
	}

}
