package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin extends Item {
    public FlintAndSteelMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (Tweaks.CONFIG.flintAndSteel.enabled && !entity.isFireImmune()) {
            entity.setOnFireFor(Tweaks.CONFIG.flintAndSteel.burnTime);
            stack.damage(1, user, ((p) -> p.sendToolBreakStatus(hand)));
            return true;
        } else {
            return false;
        }
    }
}
