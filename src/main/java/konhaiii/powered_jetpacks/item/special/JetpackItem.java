package konhaiii.powered_jetpacks.item.special;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import team.reborn.energy.api.base.SimpleEnergyItem;

public class JetpackItem extends Item implements SimpleEnergyItem, Equipment {
	private final int maxEnergy;
	private final int inputEnergy;
	private final float flightPower;
	private final float flightSpeed;
	private final int energyCost;

	public JetpackItem(int maxEnergy, int inputEnergy, float flightPower, float flightSpeed, int energyCost) {
		super(new Settings().maxCount(1));
		this.maxEnergy = maxEnergy;
		this.inputEnergy = inputEnergy;
		this.flightPower = flightPower;
		this.flightSpeed = flightSpeed;
		this.energyCost = energyCost;
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
	}

	@Override
	public long getEnergyCapacity(ItemStack itemStack) {
		return maxEnergy;
	}

	@Override
	public long getEnergyMaxInput(ItemStack itemStack) {
		return inputEnergy;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack itemStack) {
		return 0;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.round(getStoredEnergy(stack) * 13.f / getEnergyCapacity(stack));
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0xff8006;
	}

	@Override
	public EquipmentSlot getSlotType() {
		return EquipmentSlot.CHEST;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return this.equipAndSwap(this, world, user, hand);
	}

	public void extractEnergy(ItemStack stack, boolean simulate) {
		long energyStored = getStoredEnergy(stack);
		long energyExtracted = Math.min(energyStored, energyCost);
		if (!simulate) {
			setStoredEnergy(stack, energyStored - energyExtracted);
		}
	}

	public double addToVerticalVelocity(double velocityY) {
		return Math.min(velocityY + (0.1*flightPower), (0.25*flightPower));
	}

	public float getFlightSpeed() {
		return flightSpeed;
	}

	public int getEnergyCost() {
		return energyCost;
	}
}
