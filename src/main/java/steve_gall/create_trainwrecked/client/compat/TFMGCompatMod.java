package steve_gall.create_trainwrecked.client.compat;

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
		return TFMGCompatModClient::init;
	}

}
