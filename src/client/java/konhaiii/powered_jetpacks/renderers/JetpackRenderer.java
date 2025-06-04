package konhaiii.powered_jetpacks.renderers;

import konhaiii.powered_jetpacks.PoweredJetpacks;
import konhaiii.powered_jetpacks.item.ModItems;
import konhaiii.powered_jetpacks.item.special.JetpackItem;
import konhaiii.powered_jetpacks.models.JetpackModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JetpackRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private final JetpackModel<T> jetpackModel;
	private static final Identifier BASIC_JETPACK_TEXTURE = new Identifier(PoweredJetpacks.MOD_ID, "textures/jetpack/basic_jetpack.png");
	private static final Identifier ADVANCED_JETPACK_TEXTURE = new Identifier(PoweredJetpacks.MOD_ID, "textures/jetpack/advanced_jetpack.png");
	private static final Identifier INDUSTRIAL_JETPACK_TEXTURE = new Identifier(PoweredJetpacks.MOD_ID, "textures/jetpack/industrial_jetpack.png");

	public JetpackRenderer(FeatureRendererContext<T, M> context) {
		super(context);
		this.jetpackModel = new JetpackModel<>();
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		ItemStack chestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
		ItemStack backStack = ItemStack.EMPTY;
		if (PoweredJetpacks.isTrinketsLoaded) {
			try {
				Class<?> optionalClass = Class.forName("konhaiii.powered_jetpacks.compat.TrinketsClient");
				Method getBackStackMethod = optionalClass.getMethod("getBackStackLivingEntity", LivingEntity.class);
				backStack = (ItemStack) getBackStackMethod.invoke(null, entity);
			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
			         IllegalAccessException e) {
				PoweredJetpacks.LOGGER.error("Could not load Trinkets compat class.");
			}
		}

		ItemStack jetpackStack = isValidJetpack(chestStack) ? chestStack : (!backStack.isEmpty() ? backStack : ItemStack.EMPTY);

		if (!jetpackStack.isEmpty()) {
			matrixStack.push();
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			matrixStack.translate(-0.5D, -0.7D, -0.8D);
			this.jetpackModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			VertexConsumer vertexConsumer;
			if (jetpackStack.getItem() == ModItems.BASIC_JETPACK) {
				vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(BASIC_JETPACK_TEXTURE));
			} else if (jetpackStack.getItem() == ModItems.ADVANCED_JETPACK) {
				vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(ADVANCED_JETPACK_TEXTURE));
			} else {
				vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(INDUSTRIAL_JETPACK_TEXTURE));
			}

			this.jetpackModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

			matrixStack.pop();
		}
	}

	private boolean isValidJetpack(ItemStack stack) {
		return stack.getItem() instanceof JetpackItem;
	}
}