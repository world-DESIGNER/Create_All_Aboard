package steve_gall.create_trainwrecked.common.mixin.contraption;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.contraptions.MountedStorageManager;

import net.minecraft.core.BlockPos;

@Mixin(value = MountedStorageManager.class, remap = false)
public interface MountedStorageManagerAccessor
{
	@Accessor
	Map<BlockPos, MountedFluidStorage> getFluidStorage();
}
