package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.util.EntityProvider;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShapeContext.class)
public class ExtendShapeContext implements EntityProvider, ItemConvertible {
    @Mutable @Shadow @Final private Item heldItem;
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
        if (this.heldItem == null) {
            this.heldItem = this.entity instanceof LivingEntity ? ((LivingEntity)this.entity).getMainHandStack().getItem() : Items.AIR;
        }

        return this.heldItem;
    }
}
