package steve_gall.create_trainwrecked.common.content.contraption;

import com.simibubi.create.content.contraptions.MountedFluidStorage;

import net.minecraft.core.BlockPos;

public interface MountedStorageManagerExtension
{
	MountedFluidStorage getfluidStorage(BlockPos localPos);
}
