package konhaiii.powered_jetpacks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import konhaiii.powered_jetpacks.PoweredJetpacks;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ModConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = Path.of("config", "powered_jetpacks.json");

	public int basicJetpackMaxEnergy = 10000;
	public int basicJetpackInputEnergy = 128;
	public float basicJetpackVerticalSpeed = 1.0f;
	public float basicJetpackHorizontalSpeed = 0.0f;
	public int basicJetpackEnergyCost = 2;
	public int advancedJetpackMaxEnergy = 100000;
	public int advancedJetpackInputEnergy = 2048;
	public float advancedJetpackVerticalSpeed = 1.5f;
	public float advancedJetpackHorizontalSpeed = 0.05f;
	public int advancedJetpackEnergyCost = 16;
	public int industrialJetpackMaxEnergy = 1000000;
	public int industrialJetpackInputEnergy = 8192;
	public float industrialJetpackVerticalSpeed = 2.0f;
	public float industrialJetpackHorizontalSpeed = 0.1f;
	public int industrialJetpackEnergyCost = 64;

	public static ModConfig loadConfig() {
		if (!Files.exists(CONFIG_PATH)) {
			ModConfig defaultConfig = new ModConfig();
			defaultConfig.saveConfig();
			return defaultConfig;
		}

		try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
			return GSON.fromJson(reader, ModConfig.class);
		} catch (IOException | JsonSyntaxException exception) {
			PoweredJetpacks.LOGGER.error(exception.getMessage());
			return new ModConfig();
		}
	}

	public void saveConfig() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException exception) {
			PoweredJetpacks.LOGGER.error(exception.getMessage());
		}
	}
}
