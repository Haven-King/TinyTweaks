package dev.hephaestus.tweaks.mixin.block;

import net.minecraft.block.DoorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DoorBlock.class)
public interface DoorBlockAccessor {
    @Invoker("getOpenSoundEventId")
    int getOpenSound();

    @Invoker("getCloseSoundEventId")
    int getCloseSound();
}
