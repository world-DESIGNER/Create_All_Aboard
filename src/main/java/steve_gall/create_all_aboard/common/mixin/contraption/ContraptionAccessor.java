package steve_gall.create_all_aboard.common.mixin.contraption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.MountedStorageManager;

@Mixin(value = Contraption.class, remap = false)
public interface ContraptionAccessor
{
	@Accessor
	MountedStorageManager getStorage();
}
