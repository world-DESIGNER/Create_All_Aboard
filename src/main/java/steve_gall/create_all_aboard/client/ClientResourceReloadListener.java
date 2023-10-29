package steve_gall.create_all_aboard.client;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import steve_gall.create_all_aboard.common.util.NumberHelper;

public class ClientResourceReloadListener implements ResourceManagerReloadListener
{
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager)
	{
		NumberHelper.update();
	}

}
