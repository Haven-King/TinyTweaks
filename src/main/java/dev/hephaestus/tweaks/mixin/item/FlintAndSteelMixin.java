package dev.hephaestus.tweaks.mixin.item;

import dev.hephaestus.tweaks.Tweaks;
import dev.hephaestus.tweaks.TweaksConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelMixin extends Item {
    public FlintAndSteelMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (TweaksConfig.Misc.FLINT_AND_STEEL.ENABLED.getValue() && !entity.isFireImmune()) {
            entity.setOnFireFor(TweaksConfig.Misc.FLINT_AND_STEEL.BURN_TIME.getValue());
            stack.damage(1, user, ((p) -> p.sendToolBreakStatus(hand)));
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
