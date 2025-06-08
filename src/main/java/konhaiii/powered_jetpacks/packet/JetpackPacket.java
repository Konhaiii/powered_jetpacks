package konhaiii.powered_jetpacks.packet;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import konhaiii.powered_jetpacks.sounds.ModSounds;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public record JetpackPacket(boolean shouldPlaySound) {
	private static final Identifier PACKET_ID = new Identifier(PoweredJetpacks.MOD_ID, "jetpack_packet");

	public static void encode(JetpackPacket packet, PacketByteBuf buf) {
		buf.writeBoolean(packet.shouldPlaySound);
	}

	public static JetpackPacket decode(PacketByteBuf buf) {
		return new JetpackPacket(buf.readBoolean());
	}

	public static void handle(ServerPlayerEntity player, JetpackPacket packet) {
		ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
		ItemStack backStack = ItemStack.EMPTY;

		if (PoweredJetpacks.isTrinketsLoaded) {
			try {
				Class<?> optionalClass = Class.forName("konhaiii.powered_jetpacks.compat.TrinketsServer");
				Method getBackStackMethod = optionalClass.getMethod("getBackStack", LivingEntity.class);
				backStack = (ItemStack) getBackStackMethod.invoke(null, player);
			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
			         IllegalAccessException e) {
				PoweredJetpacks.LOGGER.error("JetpackPacket: Could not load Trinkets compat class.");
			}
		}

		ItemStack jetpackStack = null;
		if (isValidJetpack(chestStack)) {
			jetpackStack = chestStack;
		} else if (isValidJetpack(backStack)) {
			jetpackStack = backStack;
		}

		if (jetpackStack != null) {
			JetpackItem jetpack = (JetpackItem) jetpackStack.getItem();
			player.fallDistance = 0;

			World world = player.getWorld();
			BlockPos blockPos = player.getBlockPos();

			if (packet.shouldPlaySound()) {
				if (!world.isClient) {
					world.playSound(null, blockPos, ModSounds.JETPACK_SOUND, SoundCategory.PLAYERS, 1f, 1f);
				}
			}

			Vec3d lookVec = player.getRotationVector();
			double offsetBack = -0.25;
			double offsetSide = 0.2;
			double centerX = player.getX() + lookVec.x * offsetBack;
			double centerY = player.getY() + 0.75;
			double centerZ = player.getZ() + lookVec.z * offsetBack;
			double leftX = centerX + lookVec.z * offsetSide;
			double leftZ = centerZ - lookVec.x * offsetSide;
			double rightX = centerX - lookVec.z * offsetSide;
			double rightZ = centerZ + lookVec.x * offsetSide;
			((ServerWorld) world).spawnParticles(ParticleTypes.FLAME, leftX, centerY, leftZ,1,0,0,0,0.1);
			((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, leftX, centerY, leftZ,1,0,0,0,0.1);
			((ServerWorld) world).spawnParticles(ParticleTypes.FLAME, rightX, centerY, rightZ,1,0,0,0,0.1);
			((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, rightX, centerY, rightZ,1,0,0,0,0.1);

			jetpack.extractEnergy(jetpackStack, false);
		}
	}

	private static boolean isValidJetpack(ItemStack stack) {
		if (stack.getItem() instanceof JetpackItem jetpack) {
			if (jetpack.getEnergyCost() <= 0) {
				return true;
			}
			return jetpack.getStoredEnergy(stack) > 0;
		}
		return false;
	}

	public static Identifier getPacketId() {
		return PACKET_ID;
	}
}