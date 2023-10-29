package steve_gall.create_all_aboard.common.content.train;

import java.util.List;

public record EngineSpeedPlan(List<Engine> engines, FuelBurning fuel, double eachSpeed)
{

}
