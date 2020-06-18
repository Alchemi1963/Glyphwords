package me.alchemi.glyphwords.enchantments;

import java.util.Random;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import me.alchemi.glyphwords.Config.EnchantmentOptions;

public class EnchantmentExplosiveTip extends Enchantment {
	
	public EnchantmentExplosiveTip() {
		super("explosive_tip", EnchantmentType.RANGED_WEAPON);
		chance = EnchantmentOptions.EXPLOSIVE_TIP_CHANCE.asDouble();
		maxLevel = 1;
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return item.containsEnchantment(org.bukkit.enchantments.Enchantment.ARROW_FIRE);
	}

	@Override
	public boolean isConflicting(Enchantment enchant) {
		return false;
	}

	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return enchant == org.bukkit.enchantments.Enchantment.ARROW_FIRE;
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (e.getEntity() == null || e.getEntityType() != EntityType.PLAYER || !hasItem(e.getBow())) return;
		
		if (e.getProjectile() instanceof AbstractArrow) {
			if (new Random().nextInt(100) <= EnchantmentOptions.EXPLOSIVE_TIP_EXPLOSIONCHANCE.asDouble()) e.getProjectile().setCustomName("explosive");
		}
		
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof AbstractArrow && !e.getEntity().getCustomName().isEmpty() && e.getEntity().getCustomName().equals("explosive")) {
			
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), EnchantmentOptions.EXPLOSIVE_TIP_POWER.asInt(), 
					EnchantmentOptions.EXPLOSIVE_TIP_SETFIRE.asBoolean(), EnchantmentOptions.EXPLOSIVE_TIP_DAMAGEBLOCKS.asBoolean());
			e.getEntity().remove();
			
		}
	}
	
}
