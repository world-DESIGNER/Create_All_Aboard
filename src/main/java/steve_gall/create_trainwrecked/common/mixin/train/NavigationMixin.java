package steve_gall.create_trainwrecked.common.mixin.train;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.simibubi.create.content.trains.entity.Navigation;

import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

@Mixin(value = Navigation.class, remap = false)
public abstract class NavigationMixin
{
	@ModifyVariable(method = "findNearestApproachable", at = @At("STORE"), ordinal = 0)
	private double findNearestApproachable(double acceleration)
	{
		return TrainHelper.deacceleration(((Navigation) (Object) this).train);
	}

	@ModifyVariable(method = "tick", at = @At("STORE"), ordinal = 0)
	private double tick(double acceleration)
	{
		return TrainHelper.deacceleration(((Navigation) (Object) this).train);
	}

}
