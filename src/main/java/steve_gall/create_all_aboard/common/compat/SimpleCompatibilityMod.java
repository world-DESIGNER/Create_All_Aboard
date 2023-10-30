package steve_gall.create_all_aboard.common.compat;

import java.util.function.Supplier;

public class SimpleCompatibilityMod extends CompatibilityMod
{
	public static Supplier<CompatibilityMod> of(String modId)
	{
		return () -> new SimpleCompatibilityMod(modId);
	}

	private final String modId;

	public SimpleCompatibilityMod(String modId)
	{
		this.modId = modId;
	}

	@Override
	protected String getModId()
	{
		return this.modId;
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

}
