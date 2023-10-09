package steve_gall.create_trainwrecked.client.mixin.flywheel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.AbstractInstance;

@Mixin(value = AbstractInstance.class, remap = false)
public interface AbstractInstanceAccessor
{
	@Accessor
	MaterialManager getMaterialManager();
}
