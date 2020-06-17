package me.alchemi.glyphwords.enchantments;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantmentXP extends Enchantment {

	public EnchantmentXP() {
		super("xp_boost", (short)3, EnchantmentType.WEAPON);
	}
	
	@EventHandler
	public void deathEvent(EntityDeathEvent e) {
		if (e.getEntity().getKiller() == null || isPlayerHolding(e.getEntity().getKiller()) == null) return;
		
		System.out.println(e.getDroppedExp());
		short level = getLevel(e.getEntity().getKiller().getInventory().getItemInMainHand());
		e.setDroppedExp((int) (e.getDroppedExp() * 1.5 * level));
		System.out.println(e.getDroppedExp());
	}

	@Override
	boolean hasConflicting(ItemStack item) {
		return false;
	}
	
}
