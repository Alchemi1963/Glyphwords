package me.alchemi.glyphwords.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.EnchantmentManager.Enchantments;
import me.alchemi.glyphwords.util.EnchantUtil;
import me.alchemi.glyphwords.util.MetaTaskId;

public class EnchantmentDamageNoon extends Enchantment {

	public EnchantmentDamageNoon() {
		super("damage_noon", EnchantmentType.MELEE_WEAPON);
		chance = EnchantmentOptions.DAMAGE_FULLMOON_CHANCE.asDouble();
		maxLevel = Short.parseShort(EnchantmentOptions.DAMAGE_FULLMOON_MAXLEVEL.asString());
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return EnchantUtil.getEnchantments(item).contains(Enchantments.DAMAGE_FULLMOON.getInstance());
	}

	@Override
	public boolean isConflicting(Enchantment enchant) {
		return enchant.equals(Enchantments.DAMAGE_FULLMOON.getInstance());
	}

	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return false;
	}

	@EventHandler
	public void onHeldItem(PlayerItemHeldEvent e) {
		ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (!hasItem(item)){
			if (e.getPlayer().hasMetadata("holding_noon")) {
				Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("holding_noon").get(0).asInt());
				e.getPlayer().removeMetadata("holding_noon", Glyph.getInstance());
			}
			return;
		} else if (e.getPlayer().getWorld().getTime() >= 5000 && e.getPlayer().getWorld().getTime() <= 7000) {
			e.getPlayer().setMetadata("holding_noon", new MetaTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Glyph.getInstance(), new Runnable() {
				@Override
				public void run() {
					
					if (e.getPlayer().getWorld().getTime() >= 5000 && e.getPlayer().getWorld().getTime() <= 7000) {
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, getLevel(item) - 1, false, false));
					} else {
						Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("holding_noon").get(0).asInt());
						e.getPlayer().removeMetadata("holding_noon", Glyph.getInstance());
					}
					
				}
			}, 0, 10)));
		}		
	}	
}