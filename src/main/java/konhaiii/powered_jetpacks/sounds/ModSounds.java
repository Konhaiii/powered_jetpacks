package konhaiii.powered_jetpacks.sounds;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
	public static final SoundEvent JETPACK_SOUND = registerSoundEvent();

	private static SoundEvent registerSoundEvent() {
		Identifier id = new Identifier(PoweredJetpacks.MOD_ID, "jetpack_use");
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void initialize() {
	}
}