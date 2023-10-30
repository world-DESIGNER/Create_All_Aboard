package steve_gall.create_all_aboard.common.compat;

public class TFMGCompatMod extends CompatibilityMod
{
	public static final String MOD_ID = "createindustry";

	@Override
	public String getModId()
	{
		return MOD_ID;
	}

	@Override
	protected void onLoadCommon()
	{

	}

	@Override
	protected Runnable getClientInitializer()
	{
		return null;
	}

	@Override
	public void initPonders()
	{
		super.initPonders();

		TFMGCompatPonders.init();
	}

}
