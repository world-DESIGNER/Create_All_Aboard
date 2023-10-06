package steve_gall.create_trainwrecked.common.content.train;

import java.util.List;

public record EngineSpeedPlan(List<Engine> engines, FuelBurning fuel, double eachSpeed)
{

}
