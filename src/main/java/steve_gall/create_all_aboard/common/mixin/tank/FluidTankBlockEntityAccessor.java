package steve_gall.create_all_aboard.common.mixin.tank;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

@Mixin(value = FluidTankBlockEntity.class)
public interface FluidTankBlockEntityAccessor
{
	@Invoker(remap = false)
	void invokeRefreshCapability();
}
