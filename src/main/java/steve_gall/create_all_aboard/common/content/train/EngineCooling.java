package steve_gall.create_all_aboard.common.content.train;

public record EngineCooling(double toCool, double cooled)
{
	public boolean isUsed()
	{
		return this.toCool() > 0.0D ? (this.cooled() > 0.0D) : true;
	}

	public double getCooledRatio()
	{
		return this.toCool() > 0.0D ? (this.cooled() / this.toCool()) : 1.0D;
	}

}
