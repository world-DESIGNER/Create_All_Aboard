package steve_gall.create_trainwrecked.common.content.train;

public record FuelBurning(double toBurn, double burned)
{
	public boolean isUsed()
	{
		return this.toBurn() > 0.0D ? (this.burned() > 0.0D) : true;
	}

	public double getUsedRatio()
	{
		return this.toBurn() > 0.0D ? (this.burned() / this.toBurn()) : 1.0D;
	}

}
