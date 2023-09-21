package steve_gall.create_trainwrecked.common.content.train;

public interface TrainExtension
{
	FuelBurner getFuelBurner();

	CoolingSystem getCoolingSystem();

	void setApproachAccelerationMod(float accelerationMod);

	float getApproachAccelerationMod();
}
