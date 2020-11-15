package dev.hephaestus.tweaks.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Items.class)
public class CharcoalDoesntBurnMixin {
	@SuppressWarnings("UnresolvedMixinReference")
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/item/Item", ordinal = 3))
	private static Item charcoalShouldntBurn(Item.Settings settings) {
		return new Item(settings.fireproof());
	}
}
