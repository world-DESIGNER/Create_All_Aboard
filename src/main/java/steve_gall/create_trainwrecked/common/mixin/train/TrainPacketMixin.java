package steve_gall.create_trainwrecked.common.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainPacket;
import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.network.FriendlyByteBuf;
import steve_gall.create_trainwrecked.common.content.train.CarriageExtension;
import steve_gall.create_trainwrecked.common.content.train.Engine;
import steve_gall.create_trainwrecked.common.content.train.HeatSource;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

@Mixin(value = TrainPacket.class, remap = false)
public abstract class TrainPacketMixin extends SimplePacketBase
{
	@Shadow
	private Train train;

	@Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "TAIL"))
	private void init(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		for (Carriage carriage : this.train.carriages)
		{
			CarriageExtension carriageExtension = (CarriageExtension) carriage;
			carriageExtension.getEngines().addAll(TrainHelper.readTrainParts(buffer, Engine::new));
			carriageExtension.getHeatSources().addAll(TrainHelper.readTrainParts(buffer, HeatSource::new));
		}

	}

	@Inject(method = "write", at = @At(value = "TAIL"), cancellable = true)
	private void write(FriendlyByteBuf buffer, CallbackInfo ci)
	{
		for (Carriage carriage : this.train.carriages)
		{
			CarriageExtension carriageExtension = (CarriageExtension) carriage;
			TrainHelper.writeTrainParts(buffer, carriageExtension.getEngines());
			TrainHelper.writeTrainParts(buffer, carriageExtension.getHeatSources());
		}

	}

}
