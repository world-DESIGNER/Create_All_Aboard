package steve_gall.create_trainwrecked.common.content.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.NBTHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import steve_gall.create_trainwrecked.common.content.contraption.MountedStorageManagerExtension;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;

public class FuelBurner
{
	private Map<FluidTagEntry, FuelStatus> map;

	public FuelBurner()
	{
		this.map = new HashMap<>();
	}

	public FuelBurner(CompoundTag tag)
	{
		this();
		this.readNbt(tag);
	}

	public FuelBurner(FriendlyByteBuf buffer)
	{
		this();
		this.readNetwork(buffer);
	}

	public double burn(Train train, List<FluidTagEntry> fuelType, double amount, boolean simulate)
	{
		double totalBurned = 0.0D;

		for (FluidTagEntry entry : fuelType)
		{
			if (amount <= 0.0D)
			{
				break;
			}

			double burned = this.map.computeIfAbsent(entry, FuelStatus::new).burn(train, amount, simulate);
			totalBurned += burned;
			amount -= burned;
		}

		return totalBurned;
	}

	public void readNbt(CompoundTag tag)
	{
		this.map.clear();
		NBTHelper.iterateCompoundList(tag.getList("map", Tag.TAG_COMPOUND), c ->
		{
			FuelStatus status = new FuelStatus(c);
			this.map.put(status.getType(), status);
		});
	}

	public CompoundTag writeNbt()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("map", NBTHelper.writeCompoundList(new ArrayList<>(this.map.values()), FuelStatus::toNbt));

		return tag;
	}

	public void readNetwork(FriendlyByteBuf buffer)
	{
		this.map.clear();
		for (FuelStatus status : buffer.readList(FuelStatus::new))
		{
			this.map.put(status.getType(), status);
		}

	}

	public void writeNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeCollection(this.map.values(), FuelStatus::toNetwork);
	}

	public class FuelStatus
	{
		private final FluidTagEntry type;
		private double remained;

		public FuelStatus(FluidTagEntry type)
		{
			this.type = type;
		}

		public FuelStatus(CompoundTag tag)
		{
			this.type = FluidTagEntry.TYPE.fromNbt(tag.get("type"));
			this.remained = tag.getDouble("remained");
		}

		public FuelStatus(FriendlyByteBuf buffer)
		{
			this.type = FluidTagEntry.TYPE.fromNetwork(buffer);
			this.remained = buffer.readDouble();
		}

		public double burn(Train train, double toBurn, boolean simulate)
		{
			int extracting = Mth.ceil(Math.max(toBurn - this.remained, 0.0D));
			int extracted = 0;

			if (extracting > 0)
			{
				for (Carriage carriage : train.carriages)
				{
					TrainCargoManager storage = carriage.storage;
					IFluidHandler fluidStroage = storage.getFluids();

					if (fluidStroage == null)
					{
						// Work on client
						fluidStroage = ((MountedStorageManagerExtension) storage).getSyncedFluids();
					}

					for (FluidStack fluidStack : this.type.getMatchingStacks().toList())
					{
						if (extracted >= extracting)
						{
							break;
						}

						FluidStack burning = FluidHelper.deriveAmount(fluidStack, extracting - extracted);
						FluidStack burned = fluidStroage.drain(burning, simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE);

						if (!burned.isEmpty())
						{
							extracted += burned.getAmount();
						}

					}

				}

			}

			double burned = 0.0D;

			if (extracted + this.remained >= toBurn)
			{
				burned = toBurn;

				if (!simulate)
				{
					this.remained += extracted - toBurn;
				}
			}
			else
			{
				burned = extracted + this.remained;

				if (!simulate)
				{
					this.remained = 0.0D;
				}

			}

			return burned;
		}

		public CompoundTag toNbt()
		{
			CompoundTag tag = new CompoundTag();
			tag.put("type", this.type.toNbt());
			tag.putDouble("remained", this.remained);

			return tag;
		}

		public static void toNetwork(FriendlyByteBuf buffer, FuelStatus status)
		{
			FluidTagEntry.TYPE.toNetwork(buffer, status.type);
			buffer.writeDouble(status.remained);
		}

		public FluidTagEntry getType()
		{
			return this.type;
		}

	}

}
