package konhaiii.powered_jetpacks.models;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;

import java.util.*;

public class JetpackModel<T extends LivingEntity> extends EntityModel<T> {
	private final ModelPart middle;
	private final ModelPart bottomLeft;
	private final ModelPart bottomRight;
	private final ModelPart mainLeft;
	private final ModelPart mainRight;

	public JetpackModel() {
		// Middle part
		Map<Direction, UV> middleUVs = Map.of(
				Direction.NORTH, new UV(8, 2, 16, 16),
				Direction.EAST, new UV(6, 2, 16, 16),
				Direction.SOUTH, new UV(4, 2, 16, 16),
				Direction.WEST, new UV(10, 2, 16, 16),
				Direction.UP, new UV(4, 4, 16, 16),
				Direction.DOWN, new UV(6, 6, 16, 16)
		);
		this.middle = createCuboid(7, 2, 7, 2, 8, 2, middleUVs);

		// Bottom left part
		Map<Direction, UV> bottomLeftUVs = Map.of(
				Direction.NORTH, new UV(4, 0, 16, 16),
				Direction.EAST, new UV(2, 0, 16, 16),
				Direction.SOUTH, new UV(0, 0, 16, 16),
				Direction.WEST, new UV(6, 0, 16, 16),
				Direction.UP, new UV(2, 0, 16, 16),
				Direction.DOWN, new UV(4, 0, 16, 16)
		);
		this.bottomLeft = createCuboid(4, 0, 7, 2, 1, 2, bottomLeftUVs);

		// Bottom right part
		Map<Direction, UV> bottomRightUVs = Map.of(
				Direction.NORTH, new UV(4, 0, 16, 16),
				Direction.EAST, new UV(2, 0, 16, 16),
				Direction.SOUTH, new UV(0, 0, 16, 16),
				Direction.WEST, new UV(6, 0, 16, 16),
				Direction.UP, new UV(2, 0, 16, 16),
				Direction.DOWN, new UV(4, 0, 16, 16)
		);
		this.bottomRight = createCuboid(10, 0, 7, 2, 1, 2, bottomRightUVs);

		// Main left part
		Map<Direction, UV> mainLeftUVs = Map.of(
				Direction.NORTH, new UV(8, 0, 16, 16),
				Direction.EAST, new UV(4, 0, 16, 16),
				Direction.SOUTH, new UV(0, 0, 16, 16),
				Direction.WEST, new UV(12, 0, 16, 16),
				Direction.UP, new UV(4, 0, 16, 16),
				Direction.DOWN, new UV(4, 0, 16, 16)
		);
		this.mainLeft = createCuboid(3, 1, 6, 4, 10, 4, mainLeftUVs);

		// Main right part
		Map<Direction, UV> mainRightUVs = Map.of(
				Direction.NORTH, new UV(8, 0, 16, 16),
				Direction.EAST, new UV(4, 0, 16, 16),
				Direction.SOUTH, new UV(0, 0, 16, 16),
				Direction.WEST, new UV(12, 0, 16, 16),
				Direction.UP, new UV(4, 0, 16, 16),
				Direction.DOWN, new UV(4, 0, 16, 16)
		);
		this.mainRight = createCuboid(9, 1, 6, 4, 10, 4, mainRightUVs);
	}

	private ModelPart createCuboid(int x, int y, int z, int width, int height, int depth, Map<Direction, UV> faceUVs) {
		List<ModelPart.Cuboid> cuboids = new ArrayList<>();

		for (Direction face : faceUVs.keySet()) {
			UV uv = faceUVs.get(face);
			ModelPart.Cuboid cuboid = new ModelPart.Cuboid(
					uv.u, uv.v,
					x, y, z,
					width, height, depth,
					0, 0, 0,
					false,
					uv.uWidth, uv.vHeight,
					EnumSet.of(face)
			);
			cuboids.add(cuboid);
		}

		return new ModelPart(cuboids, Map.of());
	}

	public record UV(int u, int v, int uWidth, int vHeight) {
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		middle.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bottomLeft.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		bottomRight.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		mainLeft.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		mainRight.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}