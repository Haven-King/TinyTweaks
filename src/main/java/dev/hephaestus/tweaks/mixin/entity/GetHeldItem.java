package dev.hephaestus.tweaks.mixin.entity;

import dev.hephaestus.tweaks.util.ItemEntityShapeContext;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityShapeContext.class)
public class GetHeldItem implements ItemEntityShapeContext {
	@Shadow @Final private Item heldItem;

	@Override
	public Item getItem() {
		return this.heldItem;
	}
}
