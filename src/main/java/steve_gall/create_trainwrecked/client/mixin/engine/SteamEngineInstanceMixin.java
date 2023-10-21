package steve_gall.create_trainwrecked.client.mixin.engine;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineInstance;

import steve_gall.create_trainwrecked.client.backend.instancing.InstanceHelper;
import steve_gall.create_trainwrecked.common.content.train.Engine;

@Mixin(value = SteamEngineInstance.class, remap = false)
public abstract class SteamEngineInstanceMixin extends BlockEntityInstance<SteamEngineBlockEntity>
{
	@Unique
	private Supplier<Engine> engineSupplier = InstanceHelper.getEngineSupplier(this);

	public SteamEngineInstanceMixin(MaterialManager materialManager, SteamEngineBlockEntity blockEntity)
	{
		super(materialManager, blockEntity);
	}

	@ModifyVariable(method = "beginFrame", at = @At("STORE"), ordinal = 0)
	private Float getEngineTargetAngle(Float angle)
	{
		return InstanceHelper.getEngineAnimatingAngle(this.engineSupplier.get(), angle);
	}

}
