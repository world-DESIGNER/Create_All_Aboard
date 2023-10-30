package steve_gall.create_all_aboard.common.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;

public abstract class CompatibilityMod
{
	private boolean loaded;

	public CompatibilityMod()
	{

	}

	public void load()
	{
		this.loaded = ModList.get().isLoaded(this.getModId());

		if (this.isLoaded())
		{
			this.onLoadCommon();

			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
			{
				Runnable client = this.getClientInitializer();

				if (client != null)
				{
					client.run();
				}

			});

		}

	}

	protected abstract String getModId();

	protected abstract void onLoadCommon();

	protected abstract Runnable getClientInitializer();

	public boolean isLoaded()
	{
		return this.loaded;
	}

	public ResourceLocation asResource(String path)
	{
		return new ResourceLocation(this.getModId(), path);
	}

	public void initPonders()
	{

	}

}
