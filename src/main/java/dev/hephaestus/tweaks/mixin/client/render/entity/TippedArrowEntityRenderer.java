package dev.hephaestus.tweaks.mixin.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ProjectileEntityRenderer.class)
public abstract class TippedArrowEntityRenderer<T extends PersistentProjectileEntity> extends EntityRenderer<T> {
	protected TippedArrowEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	private void drawArrowHead(T persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (persistentProjectileEntity instanceof ArrowEntity && ((ArrowEntity) persistentProjectileEntity).getColor() != 0) {
			int color = ((ArrowEntity) persistentProjectileEntity).getColor();
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(persistentProjectileEntity)));
			MatrixStack.Entry entry = matrixStack.peek();
			Matrix4f matrix4f = entry.getModel();
			Matrix3f matrix3f = entry.getNormal();

			for (int u = 0; u < 4; ++u) {
				matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				draw(matrix4f, matrix3f, vertexConsumer, 5, -2, 0.40625F, 0, i, color);
				draw(matrix4f, matrix3f, vertexConsumer, 8, -2, 0.5F, 0, i, color);
				draw(matrix4f, matrix3f, vertexConsumer, 8, 2, 0.5F, 0.15625F, i, color);
				draw(matrix4f, matrix3f, vertexConsumer, 5, 2, 0.40625F, 0.15625F, i, color);
			}
		}
	}

	private void draw(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int i, int j, float f, float g, int o, int color) {
		vertexConsumer.vertex(matrix4f, (float)i, (float)j, 0.001F).color(color >> 16 & 255, color >> 8 & 255, color & 255, 128).texture(f, g).overlay(OverlayTexture.DEFAULT_UV).light(o).normal(matrix3f, (float) 0, (float) 0, (float) 1).next();
	}
}
