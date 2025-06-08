package konhaiii.powered_jetpacks.mixin.client;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import konhaiii.powered_jetpacks.packet.JetpackPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Unique
	private int soundCounter = 8;

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
		if (player.input.jumping) {
			ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
			ItemStack backStack = ItemStack.EMPTY;

			if (PoweredJetpacks.isTrinketsLoaded) {
				try {
					Class<?> optionalClass = Class.forName("konhaiii.powered_jetpacks.compat.TrinketsServer");
					Method getBackStackMethod = optionalClass.getMethod("getBackStack", LivingEntity.class);
					backStack = (ItemStack) getBackStackMethod.invoke(null, player);
				} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
				         IllegalAccessException e) {
					PoweredJetpacks.LOGGER.error("Could not load Trinkets compat class.");
				}
			}

			ItemStack jetpackStack = null;
			if (isValidJetpack(chestStack)) {
				jetpackStack = chestStack;
			} else if (isValidJetpack(backStack)) {
				jetpackStack = backStack;
			}

			if (jetpackStack != null) {
				PacketByteBuf buf = PacketByteBufs.create();
				JetpackItem jetpack = (JetpackItem) jetpackStack.getItem();
				Vec3d velocity = player.getVelocity();
				float horizontalBoost = jetpack.getFlightSpeed();
				player.setVelocity(
						velocity.x + (player.getRotationVector().x * horizontalBoost),
						jetpack.addToVerticalVelocity(player.getVelocity().y),
						velocity.z + (player.getRotationVector().z * horizontalBoost)
				);
				player.fallDistance = 0;

				soundCounter++;
				if (soundCounter >= 8) {
					JetpackPacket.encode(new JetpackPacket(true), buf);
					soundCounter = 0;
				} else {
					JetpackPacket.encode(new JetpackPacket(false), buf);
				}
				ClientPlayNetworking.send(JetpackPacket.getPacketId(), buf);
			} else if (soundCounter != 8) {
				soundCounter = 8;
			}
		} else if (soundCounter != 8) {
			soundCounter = 8;
		}
	}
	@Unique
	private boolean isValidJetpack(ItemStack stack) {
		if (stack.getItem() instanceof JetpackItem jetpack) {
			if (jetpack.getEnergyCost() <= 0) {
				return true;
			}
			return jetpack.getStoredEnergy(stack) > 0;
		}
		return false;
	}
}