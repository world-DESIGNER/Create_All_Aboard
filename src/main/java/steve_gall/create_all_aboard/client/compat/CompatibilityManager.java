package steve_gall.create_all_aboard.client.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class CompatibilityManager
{
	private static final List<CompatibilityMod> _MODS = new ArrayList<>();
	public static final List<CompatibilityMod> MODS = Collections.unmodifiableList(_MODS);

	public static final TFMGCompatMod THE_FACTORY_MUST_GROW = register(TFMGCompatMod::new);

	public static <MOD extends CompatibilityMod> MOD register(Supplier<MOD> supplier)
	{
		MOD mod = supplier.get();
		_MODS.add(mod);
		return mod;
	}

	public CompatibilityManager()
	{

	}

}
