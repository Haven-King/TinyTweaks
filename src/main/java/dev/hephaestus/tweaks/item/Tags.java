package dev.hephaestus.tweaks.item;

import dev.hephaestus.tweaks.Tweaks;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class Tags {
	public static final Tag<Item> PLANTABLE = TagRegistry.item(new Identifier(Tweaks.MOD_ID, "plantable"));
}
