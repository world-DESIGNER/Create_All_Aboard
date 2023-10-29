package steve_gall.create_all_aboard.client.ponder.instruction;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

public class InfinityTrackInstruction extends PonderInstruction
{
	private List<Pair<BlockPos, ElementLink<WorldSectionElement>>> elementLinkPairs;
	private boolean complete = false;
	private boolean suspened = false;

	public InfinityTrackInstruction(SceneBuilder scene, SceneBuildingUtil util, Selection selection)
	{
		List<BlockPos> posList = new ArrayList<>();
		selection.forEach(p -> posList.add(p.immutable()));
		this.elementLinkPairs = posList.stream().map(pos -> Pair.of(pos, scene.world.makeSectionIndependent(util.select.position(pos)))).toList();
	}

	@Override
	public boolean isComplete()
	{
		return this.complete;
	}

	public void setSuspened(SceneBuilder scene, boolean suspened)
	{
		scene.addInstruction(s -> this.suspened = suspened);
	}

	public boolean isSuspened()
	{
		return suspened;
	}

	@Override
	public void reset(PonderScene scene)
	{
		super.reset(scene);

		this.complete = false;
		this.suspened = false;
	}

	public void setComplete(SceneBuilder scene)
	{
		scene.addInstruction(s -> this.complete = true);
	}

	@Override
	public void tick(PonderScene scene)
	{
		if (this.suspened)
		{
			return;
		}

		BoundingBox bounds = scene.getWorld().getBounds();

		for (Pair<BlockPos, ElementLink<WorldSectionElement>> pair : this.elementLinkPairs)
		{
			Vec3 pos = VecHelper.getCenterOf(pair.getFirst());
			WorldSectionElement element = scene.resolve(pair.getSecond());

			Vec3 animatedOffset = element.getAnimatedOffset();
			Vec3 animatedPos = pos.add(animatedOffset);
			double over = 0.0D - animatedPos.x();

			if (over > 0.0D)
			{
				element.setAnimatedOffset(new Vec3(bounds.maxX() - pos.x() - over + 1.0D, 0.0D, 0.0D), true);
			}

			{
				animatedOffset = element.getAnimatedOffset();
				double delta = -1.0D / 20.0D;
				element.setAnimatedOffset(animatedOffset.add(delta, 0.0D, 0.0D), false);
			}

		}

	}

}
