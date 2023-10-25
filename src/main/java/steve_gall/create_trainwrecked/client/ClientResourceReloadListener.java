package steve_gall.create_trainwrecked.client;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import steve_gall.create_trainwrecked.common.util.NumberHelper;

public class ClientResourceReloadListener implements ResourceManagerReloadListener
{
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		NumberHelper.update();
	}

}
