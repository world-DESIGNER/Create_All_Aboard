package steve_gall.create_trainwrecked.common.content.train;

public record CarriageBlocksLimit(boolean hasLimit, int limitBlocks)
{
	public boolean isOvered(int carryingBlocks)
	{
		return this.hasLimit() && carryingBlocks > this.limitBlocks();
	}

}
