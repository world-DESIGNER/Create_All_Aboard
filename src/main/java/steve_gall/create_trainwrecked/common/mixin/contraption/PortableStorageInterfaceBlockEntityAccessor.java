package steve_gall.create_trainwrecked.common.mixin.contraption;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;

import net.minecraft.world.entity.Entity;

@Mixin(value = PortableStorageInterfaceBlockEntity.class)
public interface PortableStorageInterfaceBlockEntityAccessor
{
	@Accessor
	Entity getConnectedEntity();
}
