package steve_gall.create_trainwrecked.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class CommonConfig
{
	private ForgeConfigSpec configSpec;

	public final ConfigValue<Float> bogeyStress;

	public CommonConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		this.bogeyStress = builder.define("bogey_stress", 4.0F);

		this.configSpec = builder.build();
	}

	public ForgeConfigSpec getConfigSpec()
	{
		return this.configSpec;
	}

}
