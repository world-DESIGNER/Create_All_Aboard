package steve_gall.create_trainwrecked.common.content.train;

public record FuelBurning(boolean tried, double toBurn, double burned)
{
	public boolean isUsed()
	{
		if (this.tried())
		{
			return this.toBurn() > 0.0D && this.burned() > 0.0D;
		}
		else
		{
			return false;
		}

	}

	public double getUsedRatio()
	{
		if (this.tried())
		{
			return this.toBurn() > 0.0D ? this.burned() / this.toBurn() : 0.0D;
		}
		else
		{
			return 0.0D;
		}

	}

}
