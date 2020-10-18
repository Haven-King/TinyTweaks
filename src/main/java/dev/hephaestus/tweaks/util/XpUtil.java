package dev.hephaestus.tweaks.util;

import dev.hephaestus.tweaks.Tweaks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Map;
import java.util.Random;

public class XpUtil {
	private static final Random random = new Random();
	public static void addXp(ServerPlayerEntity player, int amount) {
		if (amount > 0) {
			Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.MENDING, player, ItemStack::isDamaged);
			if (entry != null) {
				ItemStack itemStack = entry.getValue();
				if (!itemStack.isEmpty() && itemStack.isDamaged()) {
					int i = Math.min(amount * 2, itemStack.getDamage());
					amount -= i / 2;
					itemStack.setDamage(itemStack.getDamage() - i);
				}
			}

			player.addExperience(amount);
			player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F);
		} else {
			Tweaks.log("Huh");
		}
	}
}
