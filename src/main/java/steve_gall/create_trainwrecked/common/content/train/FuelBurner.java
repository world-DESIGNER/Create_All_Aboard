package steve_gall.create_trainwrecked.common.content.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.utility.NBTHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import steve_gall.create_trainwrecked.common.fluid.FluidHelper;
import steve_gall.create_trainwrecked.common.util.FluidTagEntry;

public class FuelBurner
{
	private Map<FluidTagEntry, FuelStatus> map;

	public FuelBurner()
	{
		this.map = new HashMap<>();
	}

	public double burn(Train train, FluidTagEntry fuel, double amount)
	{
		return this.map.computeIfAbsent(fuel, FuelStatus::new).burn(train, amount);
	}

	public void read(CompoundTag tag)
	{
		this.map.clear();
		NBTHelper.iterateCompoundList(tag.getList("map", Tag.TAG_COMPOUND), c ->
		{
			FuelStatus status = new FuelStatus(c);
			this.map.put(status.getType(), status);
		});
	}

	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.put("map", NBTHelper.writeCompoundList(new ArrayList<>(this.map.values()), FuelStatus::write));

		return tag;
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
			this.type = FluidTagEntry.TYPE.fromNbt(tag.getCompound("type"));
			this.remained = tag.getDouble("remained");
		}

		public double burn(Train train, double toBurn)
		{
			int extracting = Mth.ceil(Math.max(toBurn - this.remained, 0.0D));
			int extracted = 0;

			if (extracting > 0)
			{
				for (Carriage carriage : train.carriages)
				{
					IFluidHandler fluidStroage = carriage.storage.getFluids();

					for (FluidStack fluidStack : this.type.toIngredient().getMatchingFluidStacks())
					{
						if (extracted >= extracting)
						{
							break;
						}

						FluidStack burning = FluidHelper.deriveAmount(fluidStack, extracting - extracted);
						FluidStack burned = fluidStroage.drain(burning, FluidAction.EXECUTE);

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
				this.remained += extracted - toBurn;
			}
			else
			{
				this.remained = 0.0D;
			}

			System.out.println("toBurn:" + toBurn + ", extracting: " + extracting + ", extracted: " + extracted + ", burning: " + burned + ", remained: " + this.remained);
			return burned;
		}

		public FluidTagEntry getType()
		{
			return this.type;
		}

		public CompoundTag write()
		{
			CompoundTag tag = new CompoundTag();
			tag.put("type", this.type.toNbt());
			tag.putDouble("remained", this.remained);

			return tag;
		}

	}

}
