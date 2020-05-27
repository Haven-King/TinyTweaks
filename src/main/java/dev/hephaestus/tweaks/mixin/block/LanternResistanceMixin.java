package dev.hephaestus.tweaks.mixin.block;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Blocks.class)
public class LanternResistanceMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/LanternBlock"))
    private static LanternBlock redirLantern(Block.Settings settings) {
        if (Tweaks.CONFIG.lanternBlastResistance)
            return new LanternBlock(settings.strength(3.5F, 6F));
        else
            return new LanternBlock(settings);
    }
}
