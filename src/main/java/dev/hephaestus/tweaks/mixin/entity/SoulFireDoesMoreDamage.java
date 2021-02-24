package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.TweaksConfig;
import dev.hephaestus.tweaks.util.SoulFire;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class SoulFireDoesMoreDamage implements SoulFire.FireTypeModifier {
	@Shadow
	public World world;

	@Shadow public abstract Box getBoundingBox();

	@Shadow public abstract boolean isInLava();

	@Shadow private int fireTicks;

	@Unique private SoulFire.FireType fireType = SoulFire.FireType.NORMAL;

	@Override
	public SoulFire.FireType getFireType() {
		return this.fireType;
	}

	private void setFireType(SoulFire.FireType fireType) {
		this.fireType = fireType;
	}

	@Override
	public void updateFireType() {
		if (this.isInLava()) {
			setFireType(SoulFire.FireType.NORMAL);
			return;
		}

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
							this.setFireType(SoulFire.FireType.SOUL);
						} else if (fire == Blocks.FIRE) {
							this.setFireType(SoulFire.FireType.NORMAL);
						}
					}
				}
			}
		}
	}

	@Inject(method = "setFireTicks", at = @At("HEAD"))
	private void setFireType(int ticks, CallbackInfo ci) {
		if (ticks > this.fireTicks) {
			this.updateFireType();
		}
	}

	@ModifyConstant(method = "baseTick", constant = @Constant(floatValue = 1.0F))
	private float getDamage(float damage) {
		return this.getFireType() == SoulFire.FireType.NORMAL ? damage : damage * TweaksConfig.Misc.SOUL_FIRE_DAMAGE_MODIFIER.getValue();
	}

	@Inject(method = "toTag", at = @At("TAIL"))
	private void saveFireType(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		tag.putString("TinyTweaksFireType", this.getFireType().name());
	}

	@Inject(method = "fromTag", at = @At("HEAD"))
	private void loadFireType(CompoundTag tag, CallbackInfo ci) {
		if (tag.contains("TinyTweaksFireType")) {
			this.setFireType(SoulFire.FireType.valueOf(tag.getString("TinyTweaksFireType")));
		}
	}
}
