package konhaiii.powered_jetpacks.item;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
	public static Item register(Item item, String id) {
		Identifier itemID = new Identifier(PoweredJetpacks.MOD_ID, id);

		return Registry.register(Registries.ITEM, itemID, item);
	}

	public static final Item BASIC_JETPACK = register(
			new JetpackItem(PoweredJetpacks.config.basicJetpackMaxEnergy, PoweredJetpacks.config.basicJetpackInputEnergy, PoweredJetpacks.config.basicJetpackVerticalSpeed, PoweredJetpacks.config.basicJetpackHorizontalSpeed, PoweredJetpacks.config.basicJetpackEnergyCost),
			"basic_jetpack"
	);

	public static final Item ADVANCED_JETPACK = register(
			new JetpackItem(PoweredJetpacks.config.advancedJetpackMaxEnergy, PoweredJetpacks.config.advancedJetpackInputEnergy, PoweredJetpacks.config.advancedJetpackVerticalSpeed, PoweredJetpacks.config.advancedJetpackHorizontalSpeed, PoweredJetpacks.config.advancedJetpackEnergyCost),
			"advanced_jetpack"
	);

	public static final Item INDUSTRIAL_JETPACK = register(
			new JetpackItem(PoweredJetpacks.config.industrialJetpackMaxEnergy, PoweredJetpacks.config.industrialJetpackInputEnergy, PoweredJetpacks.config.industrialJetpackVerticalSpeed, PoweredJetpacks.config.industrialJetpackHorizontalSpeed, PoweredJetpacks.config.industrialJetpackEnergyCost),
			"industrial_jetpack"
	);

	public static void initialize() {
		Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP);
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.add(BASIC_JETPACK);
			itemGroup.add(ADVANCED_JETPACK);
			itemGroup.add(INDUSTRIAL_JETPACK);
		});
	}

	public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier(PoweredJetpacks.MOD_ID, "item_group"));
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(BASIC_JETPACK))
			.displayName(Text.translatable("itemGroup.powered_jetpacks"))
			.build();
}