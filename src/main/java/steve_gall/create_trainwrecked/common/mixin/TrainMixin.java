package steve_gall.create_trainwrecked.common.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import steve_gall.create_trainwrecked.common.content.train.FuelBurner;
import steve_gall.create_trainwrecked.common.content.train.TrainExtension;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

@Mixin(value = Train.class, remap = false)
public abstract class TrainMixin implements TrainExtension
{
	@Unique
	private FuelBurner fuelBurner = new FuelBurner();

	@Unique
	private float approachAccelerationMod = 0.0F;

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

	@Inject(method = "approachTargetSpeed", at = @At(value = "HEAD"), cancellable = true)
	private void approachTargetSpeed(float accelerationMod, CallbackInfo ci)
	{
		this.setApproachAccelerationMod(accelerationMod);
		ci.cancel();
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

	@Inject(method = "read", at = @At(value = "TAIL"), cancellable = true)
	private static void read(CompoundTag tag, Map<UUID, TrackGraph> trackNetworks, DimensionPalette dimensions, CallbackInfoReturnable<Train> cir)
	{
		((TrainExtension) cir.getReturnValue()).getFuelBurner().read(tag.getCompound("fuelBurner"));
	}

	@Inject(method = "write", at = @At(value = "TAIL"), cancellable = true)
	public void write(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir)
	{
		CompoundTag tag = cir.getReturnValue();
		tag.put("fuelBurner", this.getFuelBurner().write());
	}

	@Override
	@Unique
	public FuelBurner getFuelBurner()
	{
		return this.fuelBurner;
	}

	@Override
	@Unique
	public void setApproachAccelerationMod(float accelerationMod)
	{
		this.approachAccelerationMod = accelerationMod;
	}

	@Override
	@Unique
	public float getApproachAccelerationMod()
	{
		return this.approachAccelerationMod;
	}

}
