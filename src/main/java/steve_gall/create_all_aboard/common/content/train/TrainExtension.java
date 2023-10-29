package steve_gall.create_all_aboard.common.content.train;

public interface TrainExtension
{
	FuelBurner getFuelBurner();

	CoolingSystem getCoolingSystem();

	void setApproachAccelerationMod(float accelerationMod);

	float getApproachAccelerationMod();
}
