package steve_gall.create_trainwrecked.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.trains.entity.Train;

import net.minecraft.world.level.Level;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

@Mixin(value = Train.class, remap = false)
public abstract class TrainMixin
{
	@Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
	private void tick(Level level, CallbackInfo ci)
	{
		TrainHelper.tickTrain((Train) (Object) this, level);
	}

	@Inject(method = "tickPassiveSlowdown", at = @At(value = "HEAD"), cancellable = true)
	private void tickPassiveSlowdown(CallbackInfo ci)
	{
		TrainHelper.applyFuelSpeed((Train) (Object) this);
	}

	@ModifyVariable(method = "tickPassiveSlowdown", at = @At("STORE"), ordinal = 0)
	private double tickPassiveSlowdown(double acceleration)
	{
		return TrainHelper.deacceleration((Train) (Object) this);
	}

	@Inject(method = "maxSpeed", at = @At(value = "HEAD"), cancellable = true)
	private void maxSpeed(CallbackInfoReturnable<Float> cir)
	{
		float maxSpeed = TrainHelper.maxSpeed((Train) (Object) this);
		cir.setReturnValue(maxSpeed);
	}

	@Inject(method = "acceleration", at = @At(value = "HEAD"), cancellable = true)
	private void acceleration(CallbackInfoReturnable<Float> cir)
	{
		float maxSpeed = TrainHelper.acceleration((Train) (Object) this);
		cir.setReturnValue(maxSpeed);
	}

	@ModifyVariable(method = "approachTargetSpeed", at = @At("STORE"), ordinal = 1)
	public double approachTargetSpeed(double acceleration)
	{
		return TrainHelper.getSpeedDelta((Train) (Object) this);
	}

}
