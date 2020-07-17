package dev.hephaestus.tweaks.mixin.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Redirect(method = "addBlockBreakingParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/shape/VoxelShape;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box makeParticlesForEmptyBlocks(VoxelShape voxelShape) {
		return voxelShape.isEmpty() ? VoxelShapes.fullCube().getBoundingBox() : voxelShape.getBoundingBox();
	}
}
