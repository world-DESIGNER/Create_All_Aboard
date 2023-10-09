package steve_gall.create_trainwrecked.client.mixin.flywheel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine.GroupFactory;
import com.jozufozu.flywheel.core.shader.WorldProgram;

@Mixin(value = InstancingEngine.class, remap = false)
public interface InstancingEngineAccessor<P extends WorldProgram>
{
	@Accessor
	GroupFactory<P> getGroupFactory();
}
