package steve_gall.create_trainwrecked.common.util;

import java.text.NumberFormat;

public class NumberHelper
{
	private static final NumberFormat INT_FORMAT = NumberFormat.getIntegerInstance();
	private static final NumberFormat DOUBLE_FORMAT = NumberFormat.getNumberInstance();

	public static String format(long number)
	{
		return INT_FORMAT.format(number);
	}

	public static String format(double number)
	{
		return DOUBLE_FORMAT.format(number);
	}

	public static String format(double number, int decimals)
	{
		String text = format(number);
		int decimalIndex = text.indexOf('.');
		boolean hasDecimalPart = decimalIndex > -1;
		String exponentialPart = hasDecimalPart ? text.substring(0, decimalIndex) : text;
		String deciamlPart = hasDecimalPart ? text.substring(decimalIndex + 1) : "";
		int currentDecimals = hasDecimalPart ? deciamlPart.length() : 0;

		if (currentDecimals > decimals)
		{
			deciamlPart = deciamlPart.substring(0, decimals);
		}
		else if (currentDecimals < decimals)
		{
			StringBuilder zero = new StringBuilder();

			for (int i = 0; i < decimals - currentDecimals; i++)
			{
				zero.append('0');
			}

			deciamlPart += zero;
		}

		return new StringBuilder().append(exponentialPart).append(".").append(deciamlPart).toString();
	}

}
