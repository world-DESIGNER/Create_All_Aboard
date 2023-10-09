package steve_gall.create_trainwrecked.common.content.fluid.tank;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import steve_gall.create_trainwrecked.common.mixin.tank.FluidTankBlockEntityAccessor;

public class FuelTankHelper
{
	public static void resetBoiler(FluidTankBlockEntity tank)
	{
		BoilerData boiler = tank.boiler;

		if (boiler.attachedEngines != 0 || boiler.attachedWhistles != 0)
		{
			boiler.attachedEngines = 0;
			boiler.attachedWhistles = 0;
			boiler.needsHeatLevelUpdate = true;

			((FluidTankBlockEntityAccessor) tank).invokeRefreshCapability();
			tank.notifyUpdate();
		}

	}

	private FuelTankHelper()
	{

	}

}
