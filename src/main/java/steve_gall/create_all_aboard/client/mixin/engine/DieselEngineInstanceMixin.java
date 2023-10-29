package steve_gall.create_all_aboard.client.mixin.engine;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.drmangotea.createindustry.content.Engines.diesel.DieselEngineBlockEntity;
import com.drmangotea.createindustry.content.Engines.diesel.DieselEngineInstance;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;

import steve_gall.create_all_aboard.client.backend.instancing.InstanceHelper;
import steve_gall.create_all_aboard.common.content.train.Engine;

@Mixin(value = DieselEngineInstance.class, remap = false)
public abstract class DieselEngineInstanceMixin extends BlockEntityInstance<DieselEngineBlockEntity>
{
	@Unique
	private Supplier<Engine> engineSupplier = InstanceHelper.getEngineSupplier(this);

	public DieselEngineInstanceMixin(MaterialManager materialManager, DieselEngineBlockEntity blockEntity)
	{
		super(materialManager, blockEntity);
	}

	@ModifyVariable(method = "beginFrame", at = @At("STORE"), ordinal = 0)
	private Float getEngineTargetAngle(Float angle)
	{
		return InstanceHelper.getEngineAnimatingAngle(this.engineSupplier.get(), angle);
	}

}
