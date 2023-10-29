package steve_gall.create_all_aboard.common.util;

import java.util.Comparator;
import java.util.function.Function;

public class CompareUtil
{
	public static <T, V extends Comparable<V>> Comparator<T> asc(Function<T, V> func)
	{
		return (o1, o2) -> func.apply(o1).compareTo(func.apply(o2));
	}

	public static <T, V extends Comparable<V>> Comparator<T> desc(Function<T, V> func)
	{
		return (o1, o2) -> func.apply(o2).compareTo(func.apply(o1));
	}

	private CompareUtil()
	{

	}

}
