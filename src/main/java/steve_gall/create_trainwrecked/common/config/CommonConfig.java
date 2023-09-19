package steve_gall.create_trainwrecked.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fluids.FluidAttributes;

public class CommonConfig
{
	private ForgeConfigSpec configSpec;

	public final ConfigValue<Float> carriageStress;

	public final ConfigValue<Integer> oilGunCapacity;
	public final ConfigValue<Integer> oilGunTransfer;

	public CommonConfig()
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		this.carriageStress = builder.define("carriage_stress", 4.0F);
		builder.push("oil_gun");
		this.oilGunCapacity = builder.define("capacity", FluidAttributes.BUCKET_VOLUME * 4);
		this.oilGunTransfer = builder.define("transfer", FluidAttributes.BUCKET_VOLUME);
		builder.pop();

		this.configSpec = builder.build();
	}

	public ForgeConfigSpec getConfigSpec()
	{
		return this.configSpec;
	}

}
