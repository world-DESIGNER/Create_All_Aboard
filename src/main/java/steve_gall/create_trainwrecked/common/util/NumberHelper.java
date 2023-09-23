package steve_gall.create_trainwrecked.common.util;

import java.text.NumberFormat;

public class NumberHelper
{
	private static final NumberFormat INT_FORMAT = NumberFormat.getIntegerInstance();

	public static String format(long number)
	{
		return INT_FORMAT.format(number);
	}

	public static String format(double number)
	{
		return String.format("%,f", number);
	}

	public static String format(double number, int decimals)
	{
		return String.format("%,." + decimals + "f", number);
	}

}
