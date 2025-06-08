package konhaiii.powered_jetpacks.hud;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static net.minecraft.client.resource.language.I18n.translate;

public class JetpackHUD implements HudRenderCallback {

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) return;

		ItemStack chestStack = client.player.getEquippedStack(EquipmentSlot.CHEST);
		ItemStack backStack = ItemStack.EMPTY;

		if (PoweredJetpacks.isTrinketsLoaded) {
			try {
				Class<?> optionalClass = Class.forName("konhaiii.powered_jetpacks.compat.TrinketsServer");
				Method getBackStackMethod = optionalClass.getMethod("getBackStack", LivingEntity.class);
				backStack = (ItemStack) getBackStackMethod.invoke(null, client.player);
			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
			         IllegalAccessException e) {
				PoweredJetpacks.LOGGER.error("JetpackHUD: Could not load Trinkets compat class.");
			}
		}

		ItemStack jetpackStack = isValidJetpack(chestStack) ? chestStack : (!backStack.isEmpty() ? backStack : ItemStack.EMPTY);

		if (!jetpackStack.isEmpty()) {
			JetpackItem jetpack = (JetpackItem) jetpackStack.getItem();
			long energy = jetpack.getStoredEnergy(jetpackStack);
			long maxEnergy = jetpack.getEnergyCapacity(jetpackStack);

			TextRenderer textRenderer = client.textRenderer;
			int percentage = percentage(energy, maxEnergy);
			String energyText  = translate("hud.powered_jetpacks.jetpack_power").concat(String.valueOf(percentage)).concat("%");

			drawContext.drawTextWithShadow(textRenderer, energyText, 10, 10, 0xFFFFFF);
		}
	}

	private boolean isValidJetpack(ItemStack stack) {
		return stack.getItem() instanceof JetpackItem;
	}

	private int percentage(double CurrentValue, double MaxValue) {
		if (CurrentValue == 0)
			return 0;
		return (int) ((CurrentValue * 100.0f) / MaxValue);
	}
}