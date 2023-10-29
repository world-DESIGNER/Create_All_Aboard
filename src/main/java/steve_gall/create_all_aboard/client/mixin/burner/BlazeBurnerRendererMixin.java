package steve_gall.create_all_aboard.client.mixin.burner;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.create_all_aboard.common.content.train.HeatSource;
import steve_gall.create_all_aboard.common.content.train.TrainHelper;

@Mixin(value = BlazeBurnerRenderer.class)
public class BlazeBurnerRendererMixin
{
	@Shadow(remap = false)
	private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource, Level level, BlockState blockState, HeatLevel heatLevel, float animation, float horizontalAngle, boolean canDrawFlame, boolean drawGoggles, boolean drawHat, int hashCode)
	{

	}

	@Inject(method = "renderInContraption", at = @At(value = "HEAD"), cancellable = true, remap = false)
	private static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor, CallbackInfo ci)
	{
		if (context.contraption.entity instanceof CarriageContraptionEntity cce)
		{
			HeatSource heatSource = TrainHelper.getHeatSource(cce.getCarriage(), context.localPos);

			if (heatSource != null)
			{
				ci.cancel();
			}
			else
			{
				return;
			}

			Level level = context.world;
			float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
			boolean drawGoggles = context.blockEntityData.contains("Goggles");
			boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
			int hashCode = context.hashCode();

			BlockState state = context.state;
			HeatLevel heatLevel = heatSource.getLevelAsHeatLevel();
			float animation = heatSource.getFuelTime() > 0 ? 0.126F : 0.0F;
			boolean drawFlame = heatSource.getFuelTime() > 0;
			renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource, level, state, heatLevel, animation, horizontalAngle, drawFlame, drawGoggles, drawHat, hashCode);
		}

	}

}
