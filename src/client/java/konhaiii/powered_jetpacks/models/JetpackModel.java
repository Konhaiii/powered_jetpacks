package konhaiii.powered_jetpacks.models;

import konhaiii.powered_jetpacks.PoweredJetpacks;
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
		ModelPart middleTemp;
		ModelPart bottomLeftTemp;
		ModelPart bottomRightTemp;
		ModelPart mainLeftTemp;
		ModelPart mainRightTemp;
		Map<Direction, UV> middleUVs;
		Map<Direction, UV> bottomLeftUVs;
		Map<Direction, UV> bottomRightUVs;
		Map<Direction, UV> mainLeftUVs;
		Map<Direction, UV> mainRightUVs;
		if (PoweredJetpacks.isSodiumLoaded) {
			// Middle part
			middleUVs = Map.of(
					Direction.NORTH, new UV(8, 2),
					Direction.EAST, new UV(10, 2),
					Direction.SOUTH, new UV(4, 2),
					Direction.WEST, new UV(6, 2),
					Direction.UP, new UV(4, 4),
					Direction.DOWN, new UV(6, 6)
			);
			// Bottom left part
			bottomLeftUVs = Map.of(
					Direction.NORTH, new UV(4, 0),
					Direction.EAST, new UV(6, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(6, 0),
					Direction.UP, new UV(2, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Bottom right part
			bottomRightUVs = Map.of(
					Direction.NORTH, new UV(4, 0),
					Direction.EAST, new UV(6, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(6, 0),
					Direction.UP, new UV(2, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Main left part
			mainLeftUVs = Map.of(
					Direction.NORTH, new UV(8, 0),
					Direction.EAST, new UV(-4, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(4, 0),
					Direction.UP, new UV(4, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Main right part
			mainRightUVs = Map.of(
					Direction.NORTH, new UV(8, 0),
					Direction.EAST, new UV(-4, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(4, 0),
					Direction.UP, new UV(4, 0),
					Direction.DOWN, new UV(4, 0)
			);
		} else {
			// Middle part
			middleUVs = Map.of(
					Direction.NORTH, new UV(8, 2),
					Direction.EAST, new UV(6, 2),
					Direction.SOUTH, new UV(4, 2),
					Direction.WEST, new UV(10, 2),
					Direction.UP, new UV(4, 4),
					Direction.DOWN, new UV(6, 6)
			);
			// Bottom left part
			bottomLeftUVs = Map.of(
					Direction.NORTH, new UV(4, 0),
					Direction.EAST, new UV(2, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(6, 0),
					Direction.UP, new UV(2, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Bottom right part
			bottomRightUVs = Map.of(
					Direction.NORTH, new UV(4, 0),
					Direction.EAST, new UV(2, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(6, 0),
					Direction.UP, new UV(2, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Main left part
			mainLeftUVs = Map.of(
					Direction.NORTH, new UV(8, 0),
					Direction.EAST, new UV(4, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(12, 0),
					Direction.UP, new UV(4, 0),
					Direction.DOWN, new UV(4, 0)
			);
			// Main right part
			mainRightUVs = Map.of(
					Direction.NORTH, new UV(8, 0),
					Direction.EAST, new UV(4, 0),
					Direction.SOUTH, new UV(0, 0),
					Direction.WEST, new UV(12, 0),
					Direction.UP, new UV(4, 0),
					Direction.DOWN, new UV(4, 0)
			);
		}
		middleTemp = createCuboid(7, 2, 7, 2, 8, 2, middleUVs);
		bottomLeftTemp = createCuboid(4, 0, 7, 2, 1, 2, bottomLeftUVs);
		bottomRightTemp = createCuboid(10, 0, 7, 2, 1, 2, bottomRightUVs);
		mainLeftTemp = createCuboid(3, 1, 6, 4, 10, 4, mainLeftUVs);
		mainRightTemp = createCuboid(9, 1, 6, 4, 10, 4, mainRightUVs);
		this.bottomRight = bottomRightTemp;
		this.bottomLeft = bottomLeftTemp;
		this.mainLeft = mainLeftTemp;
		this.middle = middleTemp;
		this.mainRight = mainRightTemp;
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
					16, 16,
					EnumSet.of(face)
			);
			cuboids.add(cuboid);
		}

		return new ModelPart(cuboids, Map.of());
	}

	public record UV(int u, int v) {
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