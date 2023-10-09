package steve_gall.create_trainwrecked.client.mixin.contraption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.contraptions.render.ContraptionGroup;
import com.simibubi.create.content.contraptions.render.FlwContraption;

@Mixin(value = ContraptionGroup.class, remap = false)
public interface ContraptionGroupAccessor
{
	@Accessor
	FlwContraption getContraption();
}
