package konhaiii.powered_jetpacks.compat;

import dev.emi.trinkets.api.TrinketsApi;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

public class TrinketsServer {

	public static ItemStack getBackStack(LivingEntity player) {
		return TrinketsApi.getTrinketComponent(player).map(component ->
				component.getEquipped(stack -> stack.getItem() instanceof JetpackItem)
						.stream()
						.findFirst()
						.map(Pair::getRight)
						.orElse(ItemStack.EMPTY)
		).orElse(ItemStack.EMPTY);
	}
}
