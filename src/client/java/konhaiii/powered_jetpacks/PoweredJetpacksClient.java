package konhaiii.powered_jetpacks;

import konhaiii.powered_jetpacks.hud.JetpackHUD;
import konhaiii.powered_jetpacks.renderers.JetpackRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class PoweredJetpacksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register(new StackToolTipHandler());
		HudRenderCallback.EVENT.register(new JetpackHUD());
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer != null) {
				FeatureRendererContext<LivingEntity, EntityModel<LivingEntity>> featureRendererContext =
						new FeatureRendererContext<>() {
							@Override
							public EntityModel<LivingEntity> getModel() {
								return null;
							}

							@Override
							public Identifier getTexture(LivingEntity entity) {
								return null;
							}
						};
				registrationHelper.register(new JetpackRenderer<>(featureRendererContext));
			}
		});
	}
}