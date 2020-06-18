package me.alchemi.glyphwords;

import java.util.Collection;
import java.util.Set;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.enchantments.Enchantment;
import me.alchemi.glyphwords.enchantments.EnchantmentAttackFast;
import me.alchemi.glyphwords.enchantments.EnchantmentDamageFullMoon;
import me.alchemi.glyphwords.enchantments.EnchantmentDamageNoon;
import me.alchemi.glyphwords.enchantments.EnchantmentExplosiveTip;
import me.alchemi.glyphwords.enchantments.EnchantmentExtraSpeed;
import me.alchemi.glyphwords.enchantments.EnchantmentInfinityCross;
import me.alchemi.glyphwords.enchantments.EnchantmentPowerCross;
import me.alchemi.glyphwords.enchantments.EnchantmentXP;
import me.alchemi.glyphwords.util.EnchantUtil;

public class EnchantmentManager implements Listener{

	public static EnchantmentManager instance;
	
	public enum Enchantments {
		
		XP_BOOST(EnchantmentXP.class),
		INFINITY_CROSS(EnchantmentInfinityCross.class),
		POWER_CROSS(EnchantmentPowerCross.class),
		EXPLOSIVE_TIP(EnchantmentExplosiveTip.class),
		DAMAGE_FULLMOON(EnchantmentDamageFullMoon.class),
		DAMAGE_NOON(EnchantmentDamageNoon.class),
		EXTRA_SPEED(EnchantmentExtraSpeed.class),
		ATTACK_FAST(EnchantmentAttackFast.class);
		
		private Enchantment instance;
		private Class<? extends Enchantment> clazz;
		
		private Enchantments(Class<? extends Enchantment> clazz) {

			this.clazz = clazz;
			
		}
		
		public void setInstance() {
			try {
				this.instance = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		public Enchantment getInstance() {
			return instance;	
		}
		
		@Override
		public String toString() {
			return instance.getRegistryName();
		}
		
		public static Enchantment getEnchantment(String key) {
			return Enchantments.valueOf(key.toUpperCase()).getInstance();
		}
		
		public static boolean hasKey(String key) {
			
			try {
				Enchantments.valueOf(key.toUpperCase());
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void applyToItem(InventoryClickEvent e) {
		if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR || e.getAction() == InventoryAction.PLACE_ALL) {
			if (e.getCursor() == null || e.getCurrentItem() == null) return;
			
			if (e.getCursor().getType() == Options.BOOK_ITEM.asMaterial()) {
				
				ItemStack current = e.getCurrentItem().clone();
				Set<Enchantment> enchants = EnchantUtil.getEnchantments(e.getCursor());
				if (!canEnchantAny(enchants, current)) return;
				
				for (Enchantment ench : enchants) {
					current = ench.apply(current, ench.getLevel(e.getCursor()));
				}
				current = EnchantUtil.buildLore(current);
				
				e.getWhoClicked().setItemOnCursor(null);
				e.setCurrentItem(current);
				e.setResult(Result.DENY);
				
			} else if (e.getCurrentItem().getType() == Options.BOOK_ITEM.asMaterial()) {
				
				ItemStack current = e.getCursor().clone();
				Set<Enchantment> enchants = EnchantUtil.getEnchantments(e.getCurrentItem());
				if (!canEnchantAny(enchants, current)) return;				
				for (Enchantment ench : enchants) {
					current = ench.apply(current, ench.getLevel(e.getCurrentItem()));
				}
				current = EnchantUtil.buildLore(current);
				
				e.getWhoClicked().setItemOnCursor(current);
				e.setCurrentItem(null);
				e.setResult(Result.DENY);
				
			}
		}
	}
	
	public boolean canEnchantAny(Collection<? extends Enchantment> enchants, ItemStack item) {
		for (Enchantment e : enchants) {
			if (e.canEnchant(item)) return true;
		}
		return false;
	}
}
