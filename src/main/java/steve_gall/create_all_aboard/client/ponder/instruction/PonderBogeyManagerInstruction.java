package steve_gall.create_all_aboard.client.ponder.instruction;

import java.util.HashMap;
import java.util.Map;

import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;

import net.minecraft.core.BlockPos;

public class PonderBogeyManagerInstruction extends PonderInstruction
{
	private boolean isDisposed;
	private Map<BlockPos, Float> speedMap;

	public PonderBogeyManagerInstruction()
	{
		this.isDisposed = false;
		this.speedMap = new HashMap<>();
	}

	@Override
	public void tick(PonderScene scene)
	{
		PonderWorld world = scene.getWorld();

		this.speedMap.forEach((pos, speed) ->
		{
			if (world.getBlockEntity(pos) instanceof AbstractBogeyBlockEntity blockEntity)
			{
				blockEntity.animate(speed / 20.0F);
			}
		});
	}

	public void setSpeed(SceneBuilder scene, Selection selection, float speed)
	{
		scene.addInstruction(s ->
		{
			selection.forEach(pos -> this.speedMap.put(pos, speed));
		});
	}

	public void addSpeed(SceneBuilder scene, Selection selection, float speed)
	{
		scene.addInstruction(s ->
		{
			selection.forEach(pos -> this.speedMap.compute(pos, (k, v) -> v == null ? speed : (v + speed)));
		});
	}

	public void clear(SceneBuilder scene)
	{
		scene.addInstruction(s ->
		{
			for (BlockPos pos : this.speedMap.keySet().stream().toList())
			{
				this.speedMap.put(pos, 0.0F);
			}

		});

	}

	public void dispose(SceneBuilder scene)
	{
		scene.addInstruction(s ->
		{
			this.isDisposed = true;
			this.speedMap.clear();
		});

	}

	@Override
	public boolean isComplete()
	{
		return this.isDisposed;
	}

}
