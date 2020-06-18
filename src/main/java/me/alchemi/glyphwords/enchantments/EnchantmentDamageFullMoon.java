package me.alchemi.glyphwords.enchantments;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.EnchantmentManager.Enchantments;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.util.EnchantUtil;
import me.alchemi.glyphwords.util.MetaTaskId;

public class EnchantmentDamageFullMoon extends Enchantment {
	
	public EnchantmentDamageFullMoon() {
		super("damage_fullmoon", EnchantmentType.MELEE_WEAPON);
		chance = EnchantmentOptions.DAMAGE_FULLMOON_CHANCE.asDouble();
		maxLevel = Short.parseShort(EnchantmentOptions.DAMAGE_FULLMOON_MAXLEVEL.asString());
	}

	@Override
	public boolean hasConflicting(ItemStack item) {
		return EnchantUtil.getEnchantments(item).contains(Enchantments.DAMAGE_NOON.getInstance());
	}

	@Override
	public boolean isConflicting(Enchantment enchant) {
		return enchant.equals(Enchantments.DAMAGE_NOON.getInstance());
	}

	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return false;
	}

	@EventHandler
	public void onHeldItem(PlayerItemHeldEvent e) {
		ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (!hasItem(item)){
			if (e.getPlayer().hasMetadata("holding_fullmoon")) {
				Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("holding_fullmoon").get(0).asInt());
				e.getPlayer().removeMetadata("holding_fullmoon", Glyph.getInstance());
			}
			return;
		} else if ((e.getPlayer().getWorld().getFullTime()/24000)%8 == 0
				&& (e.getPlayer().getWorld().getTime() >= 13000 && e.getPlayer().getWorld().getTime() <= 23000)) {
			e.getPlayer().setMetadata("holding_fullmoon", new MetaTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Glyph.getInstance(), new Runnable() {
				@Override
				public void run() {
					
					if (e.getPlayer().getWorld().getTime() >= 13000 && e.getPlayer().getWorld().getTime() <= 23000) {
						e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10, getLevel(item) - 1, false, false));
					} else {
						Bukkit.getScheduler().cancelTask(e.getPlayer().getMetadata("holding_fullmoon").get(0).asInt());
						e.getPlayer().removeMetadata("holding_fullmoon", Glyph.getInstance());
					}
					
				}
			}, 0, 10)));
		}		
	}	
}
