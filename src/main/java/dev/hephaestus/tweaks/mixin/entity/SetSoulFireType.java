package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.SoulFire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
@Environment(EnvType.CLIENT)
public abstract class SetSoulFireType implements SoulFire.FireTypeModifier {
	@Shadow public World world;

	@Shadow public abstract Box getBoundingBox();

	@Shadow public abstract boolean isInLava();

	private SoulFire.FireType fireType = SoulFire.FireType.NORMAL;

	@Override
	public SoulFire.FireType getFireType() {
		return this.fireType;
	}

	@Override
	public void updateFireType() {
		if (this.isInLava()) {
			this.fireType = SoulFire.FireType.NORMAL;
			return;
		}

		if (this.world.isClient && Tweaks.CONFIG.blueSoulFireEffects) {
			Box box = this.getBoundingBox();
			BlockPos blockPos = new BlockPos(box.minX + 0.001D, box.minY + 0.001D, box.minZ + 0.001D);
			BlockPos blockPos2 = new BlockPos(box.maxX - 0.001D, box.maxY - 0.001D, box.maxZ - 0.001D);
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			if (this.world.isRegionLoaded(blockPos, blockPos2)) {
				for (int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
					for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
						for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
							mutable.set(i, j, k);

							Block fire = this.world.getBlockState(mutable).getBlock();
							if (fire == Blocks.SOUL_FIRE) {
								this.fireType = SoulFire.FireType.SOUL;
							} else if (fire == Blocks.FIRE) {
								this.fireType = SoulFire.FireType.NORMAL;
							}
						}
					}
				}
			}
		}
	}

	@Inject(method = "baseTick", at = @At("HEAD"))
	private void setFireType(CallbackInfo ci) {
		this.updateFireType();
	}
}
