package dev.hephaestus.tweaks.mixin.entity.easyxp;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.util.XpUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({VillagerEntity.class, WanderingTraderEntity.class})
public abstract class VillagerTradeXp extends MerchantEntity {
	public VillagerTradeXp(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique private int i;
	@Inject(method = "afterUsing", at = @At(value = "NEW", target = "net/minecraft/entity/ExperienceOrbEntity"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void captureLocals(TradeOffer offer, CallbackInfo ci, int i) {
		this.i = i;
	}

	@Redirect(method = "afterUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private boolean depositXpToPlayer(World world, Entity entity) {
		if (this.getCurrentCustomer() instanceof ServerPlayerEntity && Tweaks.CONFIG.easyXp) {
			XpUtil.addXp((ServerPlayerEntity) this.getCurrentCustomer(), i);
			return false;
		} else {
			return world.spawnEntity(entity);
		}
	}
}
