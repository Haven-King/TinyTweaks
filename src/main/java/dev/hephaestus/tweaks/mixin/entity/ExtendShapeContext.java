package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.util.EntityProvider;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShapeContext.class)
public class ExtendShapeContext implements EntityProvider, ItemConvertible {
    @Shadow @Final private Item heldItem;
    @Unique private Entity entity = null;

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void captureEntity(Entity entity, CallbackInfo ci) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public Item asItem() {
        return this.heldItem;
    }
}
