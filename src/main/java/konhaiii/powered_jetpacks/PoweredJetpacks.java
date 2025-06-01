package konhaiii.powered_jetpacks;

import konhaiii.powered_jetpacks.config.ModConfig;
import konhaiii.powered_jetpacks.item.ModItems;
import konhaiii.powered_jetpacks.packet.JetpackPacket;
import konhaiii.powered_jetpacks.sounds.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoweredJetpacks implements ModInitializer {
	public static final String MOD_ID = "powered_jetpacks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ModConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("Initialize");
		config = ModConfig.loadConfig();
		ModItems.initialize();
		ModSounds.initialize();
		ServerPlayNetworking.registerGlobalReceiver(JetpackPacket.getPacketId(), (server, player, handler, buf, responseSender) -> {
			JetpackPacket packet = JetpackPacket.decode(buf);
			server.execute(() -> JetpackPacket.handle(player, packet));
		});
	}
}