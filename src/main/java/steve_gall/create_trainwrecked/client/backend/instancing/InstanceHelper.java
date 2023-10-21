package steve_gall.create_trainwrecked.client.backend.instancing;

import java.util.function.Supplier;

import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine.GroupFactory;
import com.jozufozu.flywheel.core.shader.WorldProgram;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;

import net.minecraft.client.renderer.RenderType;
import steve_gall.create_trainwrecked.client.mixin.contraption.ContraptionGroupAccessor;
import steve_gall.create_trainwrecked.client.mixin.flywheel.AbstractInstanceAccessor;
import steve_gall.create_trainwrecked.client.mixin.flywheel.InstancingEngineAccessor;
import steve_gall.create_trainwrecked.common.content.train.Engine;
import steve_gall.create_trainwrecked.common.content.train.TrainHelper;

public class InstanceHelper
{
	@SuppressWarnings("unchecked")
	public static Contraption getContraption(BlockEntityInstance<?> instance)
	{
		if (((AbstractInstanceAccessor) instance).getMaterialManager() instanceof InstancingEngineAccessor<?> _instancingEngine)
		{
			InstancingEngine<WorldProgram> instancingEngine = (InstancingEngine<WorldProgram>) _instancingEngine;
			GroupFactory<WorldProgram> groupFactory = ((InstancingEngineAccessor<WorldProgram>) instancingEngine).getGroupFactory();

			if (groupFactory.create(instancingEngine, RenderType.solid()) instanceof ContraptionGroupAccessor group)
			{
				return group.getContraption().contraption;
			}

		}

		return null;
	}

	public static Supplier<Engine> getEngineSupplier(BlockEntityInstance<?> instance)
	{
		if (getContraption(instance) instanceof CarriageContraption cc && cc.entity instanceof CarriageContraptionEntity cce)
		{
			Engine engine = TrainHelper.getEngine(cce.getCarriage(), instance.getWorldPosition());
			return () -> engine;
		}
		else
		{
			return () -> null;
		}

	}

	public static Float getEngineTargetAngle(Engine engine, Float angle)
	{
		return engine != null ? (Float) engine.getAngle() : angle;
	}

	private InstanceHelper()
	{

	}

}
