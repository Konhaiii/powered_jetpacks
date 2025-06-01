package konhaiii.powered_jetpacks;

import konhaiii.powered_jetpacks.energy.EnergySystem;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class StackToolTipHandler implements ItemTooltipCallback {
	@Override
	public void getTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Text> tooltipLines) {
		Item item = itemStack.getItem();
		if (item instanceof JetpackItem jetpackItem) {

			if (Screen.hasShiftDown()) {
				MutableText line1 = Text.literal(EnergySystem.getEnergy(jetpackItem.getStoredEnergy(itemStack)));
				line1.append("/");
				line1.append(EnergySystem.getEnergyUnit(jetpackItem.getEnergyCapacity(itemStack)));
				line1.formatted(Formatting.GOLD);

				tooltipLines.add(1, line1);

				int percentage = percentage(jetpackItem.getStoredEnergy(itemStack), jetpackItem.getEnergyCapacity(itemStack));
				MutableText line2  = Text.literal(String.valueOf(percentage)).append("%");
				line2.append(" ");
				line2.formatted(Formatting.GRAY);
				line2.append(I18n.translate("tooltip.powered_jetpacks.power_charged"));
				tooltipLines.add(2, line2);

				double inputRate = jetpackItem.getEnergyMaxInput(itemStack);
				double outputRate = jetpackItem.getEnergyMaxOutput(itemStack);

				MutableText line3 = Text.literal("");
				if (inputRate != 0 && inputRate == outputRate){
					line3.append(I18n.translate("tooltip.powered_jetpacks.transfer_rate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(EnergySystem.getEnergyUnitDiminished(inputRate));
					line3.formatted(Formatting.GOLD);
				}
				else if(inputRate != 0){
					line3.append(I18n.translate("tooltip.powered_jetpacks.input_rate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(EnergySystem.getEnergyUnitDiminished(inputRate));
					line3.formatted(Formatting.GOLD);
				}
				else if (outputRate !=0){
					line3.append(I18n.translate("tooltip.powered_jetpacks.output_rate"));
					line3.append(" : ");
					line3.formatted(Formatting.GRAY);
					line3.append(EnergySystem.getEnergyUnitDiminished(outputRate));
					line3.formatted(Formatting.GOLD);
				}
				tooltipLines.add(3, line3);
			} else {
				MutableText line1 = Text.literal(EnergySystem.getEnergyDiminished(jetpackItem.getStoredEnergy(itemStack)));
				line1.append("/");
				line1.append(EnergySystem.getEnergyUnitDiminished(jetpackItem.getEnergyCapacity(itemStack)));
				line1.formatted(Formatting.GOLD);

				tooltipLines.add(1, line1);
			}
		}
	}

	private int percentage(double CurrentValue, double MaxValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}
