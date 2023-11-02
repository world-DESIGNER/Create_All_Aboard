package steve_gall.create_all_aboard.common.block.entity;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import steve_gall.create_all_aboard.common.init.ModBlockEntityTypes;

public class TrainSteamEngineBlockEntity extends SmartBlockEntity
{
	public WeakReference<PoweredShaftBlockEntity> target;

	public TrainSteamEngineBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntityTypes.TRAIN_STEAM_ENGINE.get(), pos, state);

		target = new WeakReference<>(null);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours)
	{

	}

	@Override
	public void tick()
	{
		super.tick();
		PoweredShaftBlockEntity shaft = getShaft();

		if (level.isClientSide() || (shaft == null) || (shaft.enginePos != null && !shaft.getBlockPos().subtract(worldPosition).equals(shaft.enginePos)))
			return;
		Direction facing = SteamEngineBlock.getFacing(getBlockState());
		if (level.isLoaded(worldPosition.relative(facing.getOpposite())))
			shaft.update(worldPosition, 0, 0);
	}

	@Override
	public void remove()
	{
		PoweredShaftBlockEntity shaft = getShaft();
		if (shaft != null)
			shaft.remove(worldPosition);
		super.remove();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected AABB createRenderBoundingBox()
	{
		return super.createRenderBoundingBox().inflate(2);
	}

	public PoweredShaftBlockEntity getShaft()
	{
		PoweredShaftBlockEntity shaft = target.get();
		if (shaft == null || shaft.isRemoved() || !shaft.canBePoweredBy(worldPosition))
		{
			if (shaft != null)
				target = new WeakReference<>(null);
			Direction facing = SteamEngineBlock.getFacing(getBlockState());
			BlockEntity anyShaftAt = level.getBlockEntity(worldPosition.relative(facing, 2));
			if (anyShaftAt instanceof PoweredShaftBlockEntity ps && ps.canBePoweredBy(worldPosition))
				target = new WeakReference<>(shaft = ps);
		}
		return shaft;
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	public Float getTargetAngle()
	{
		float angle = 0;
		BlockState blockState = getBlockState();
		if (!ModBlockEntityTypes.TRAIN_STEAM_ENGINE.get().isValid(blockState))
			return null;

		Direction facing = SteamEngineBlock.getFacing(blockState);
		PoweredShaftBlockEntity shaft = getShaft();
		Axis facingAxis = facing.getAxis();
		Axis axis = Axis.Y;

		if (shaft == null)
			return null;

		axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
		angle = KineticBlockEntityRenderer.getAngleForTe(shaft, shaft.getBlockPos(), axis);

		if (axis == facingAxis)
			return null;
		if (axis.isHorizontal() && (facingAxis == Axis.X ^ facing.getAxisDirection() == AxisDirection.POSITIVE))
			angle *= -1;
		if (axis == Axis.X && facing == Direction.DOWN)
			angle *= -1;
		return angle;
	}

}
