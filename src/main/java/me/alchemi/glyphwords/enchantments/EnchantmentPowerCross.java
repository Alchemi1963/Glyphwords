package me.alchemi.glyphwords.enchantments;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Sets;

import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.Config.Options;

public class EnchantmentPowerCross extends Enchantment {

	public EnchantmentPowerCross() {
		super("power_cross", EnchantmentType.CUSTOM);
		customType = Sets.newHashSet(Material.CROSSBOW, Options.BOOK_ITEM.asMaterial());
		chance = EnchantmentOptions.POWER_CROSS_CHANCE.asDouble();
		maxLevel = Short.parseShort(EnchantmentOptions.POWER_CROSS_MAXLEVEL.asString());
		inOffHand = true;
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return false;
	}

	@Override
	public boolean isConflicting(Enchantment enchant) {
		return false;
	}

	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return false;
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (e.getEntity() == null || e.getEntityType() != EntityType.PLAYER || !hasItem(e.getBow())) return;
		
		short level = getLevel(e.getBow());
		if (e.getProjectile().getType() == EntityType.ARROW || e.getProjectile().getType() == EntityType.SPECTRAL_ARROW) {
			AbstractArrow arrow = (AbstractArrow) e.getProjectile();
			arrow.setDamage(arrow.getDamage() + EnchantmentOptions.POWER_CROSS_POWERINCREASE.asDouble() * level);
		}
	}
	
}
