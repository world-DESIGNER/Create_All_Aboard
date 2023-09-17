package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

import net.minecraft.core.BlockPos;

public interface CarriageContraptionExtension
{
	List<EnginPos> getAssembledEnginePos();

	BlockPos toLocalPos(BlockPos globalPos);
}
