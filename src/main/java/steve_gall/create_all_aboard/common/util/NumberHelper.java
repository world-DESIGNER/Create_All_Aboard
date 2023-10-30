package steve_gall.create_all_aboard.common.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class NumberHelper
{
	private static Map<Integer, NumberFormat> FORMATS = new HashMap<>();

	public static void update()
	{
		FORMATS.clear();
	}

	public static NumberFormat getFormat(int decimals)
	{
		return FORMATS.computeIfAbsent(decimals, d ->
		{
			NumberFormat format = NumberFormat.getInstance(Minecraft.getInstance().getLanguageManager().getJavaLocale());
			format.setMaximumFractionDigits(d);
			format.setMinimumFractionDigits(d);
			format.setGroupingUsed(true);
			return format;
		});
	}

	public static String format(long number)
	{
		return getFormat(0).format(number);
	}

	public static String format(double number, int decimals)
	{
		return getFormat(decimals).format(number);
	}

}
