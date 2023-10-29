package steve_gall.create_all_aboard.common.config;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class CreateTrainwreckedConfig
{
	public static final CommonConfig COMMON = new CommonConfig();

	public static void registerConfigs(ModLoadingContext modLoadingContext)
	{
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, COMMON.getConfigSpec());
	}

	private CreateTrainwreckedConfig()
	{

	}

}
