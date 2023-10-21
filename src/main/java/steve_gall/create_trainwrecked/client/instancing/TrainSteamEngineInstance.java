package steve_gall.create_trainwrecked.client.instancing;

import java.util.function.Supplier;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import steve_gall.create_trainwrecked.client.backend.instancing.InstanceHelper;
import steve_gall.create_trainwrecked.common.block.entity.TrainSteamEngineBlockEntity;
import steve_gall.create_trainwrecked.common.content.train.Engine;

public class TrainSteamEngineInstance extends BlockEntityInstance<TrainSteamEngineBlockEntity> implements DynamicInstance
{

	protected final ModelData piston;
	protected final ModelData linkage;
	protected final ModelData connector;

	protected final Supplier<Engine> engineSupplier;

	public TrainSteamEngineInstance(MaterialManager materialManager, TrainSteamEngineBlockEntity blockEntity)
	{
		super(materialManager, blockEntity);

		piston = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_PISTON, blockState).createInstance();
		linkage = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_LINKAGE, blockState).createInstance();
		connector = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(AllPartialModels.ENGINE_CONNECTOR, blockState).createInstance();

		engineSupplier = InstanceHelper.getEngineSupplier(this);
	}

	@Override
	public void beginFrame()
	{
		Float angle = InstanceHelper.getEngineTargetAngle(this.engineSupplier.get(), null);

		if (angle == null)
		{
			piston.setEmptyTransform();
			linkage.setEmptyTransform();
			connector.setEmptyTransform();
			return;
		}

		Direction facing = SteamEngineBlock.getFacing(blockState);
		Axis facingAxis = facing.getAxis();
		Axis axis = Axis.Y;

		boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
		float sine = Mth.sin(angle);
		float sine2 = Mth.sin(angle - Mth.HALF_PI);
		float piston = ((1 - sine) / 4) * 24 / 16f;

		transformed(this.piston, facing, roll90).translate(0, piston, 0);

		transformed(linkage, facing, roll90).centre().translate(0, 1, 0).unCentre().translate(0, piston, 0).translate(0, 4 / 16f, 8 / 16f).rotateX(sine2 * 23f).translate(0, -4 / 16f, -8 / 16f);

		transformed(connector, facing, roll90).translate(0, 2, 0).centre().rotateXRadians(-angle + Mth.HALF_PI).unCentre();
	}

	protected ModelData transformed(ModelData modelData, Direction facing, boolean roll90)
	{
		return modelData.loadIdentity().translate(getInstancePosition()).centre().rotateY(AngleHelper.horizontalAngle(facing)).rotateX(AngleHelper.verticalAngle(facing) + 90).rotateY(roll90 ? -90 : 0).unCentre();
	}

	@Override
	public void updateLight()
	{
		relight(pos, piston, linkage, connector);
	}

	@Override
	protected void remove()
	{
		piston.delete();
		linkage.delete();
		connector.delete();
	}

}
