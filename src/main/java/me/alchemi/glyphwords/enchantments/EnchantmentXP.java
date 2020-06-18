package me.alchemi.glyphwords.enchantments;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.alchemi.glyphwords.Config.EnchantmentOptions;

public class EnchantmentXP extends Enchantment {

	public EnchantmentXP() {
		super("xp_boost", EnchantmentType.WEAPON);
		
		this.maxLevel = Short.parseShort(EnchantmentOptions.XP_BOOST_MAXLEVEL.asString());
		this.chance = EnchantmentOptions.XP_BOOST_CHANCE.asDouble();
	}
	
	@EventHandler
	public void deathEvent(EntityDeathEvent e) {
		if (e.getEntity().getKiller() == null || !isPlayerHolding(e.getEntity().getKiller())) return;
		
		short level = getLevel(e.getEntity().getKiller().getInventory().getItemInMainHand());
		e.setDroppedExp((int) (e.getDroppedExp() * EnchantmentOptions.XP_BOOST_MULTIPLIER.asDouble() * level));
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return false;
	}
	
	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return false;
	}
	
	@Override
	public boolean isConflicting(Enchantment enchant) {
		return false;
	}
	
}
