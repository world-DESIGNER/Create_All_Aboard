package steve_gall.create_trainwrecked.common.block.entity;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_trainwrecked.common.init.ModBlockEntityTypes;

public class TrainSteamEngineBlockEntity extends SmartBlockEntity
{
	public TrainSteamEngineBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntityTypes.TRAIN_STEAM_ENGINE.get(), pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours)
	{

	}

}
