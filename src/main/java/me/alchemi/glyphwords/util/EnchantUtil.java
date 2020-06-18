package me.alchemi.glyphwords.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Sets;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.util.NumUtil;
import me.alchemi.glyphwords.EnchantmentManager;
import me.alchemi.glyphwords.enchantments.Enchantment;

public class EnchantUtil {

	public static boolean isItemEnchanted(ItemStack item) {
		return EnchantUtil.isItemEnchanted(new NBTItem(item));
	}

	public static boolean isItemEnchanted(NBTItem nbti) {
		return nbti.hasNBTData() && nbti.hasKey("glyph.enchanted");
	}

	public static Set<Enchantment> getEnchantments(ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		if (!isItemEnchanted(nbti)) return Sets.newHashSet();
		
		Set<Enchantment> enchs = new HashSet<Enchantment>();
		NBTCompound nbtlist = nbti.getCompound("glyph.enchantments");
		for (String key : nbtlist.getKeys()) {
			if (EnchantmentManager.Enchantments.hasKey(key)) {
				enchs.add(EnchantmentManager.Enchantments.getEnchantment(key));
			}
		}
		return enchs;
	}

	public static ItemStack buildLore(ItemStack item) {
		List<String> lore = new ArrayList<String>();
		
		for (Enchantment ench : getEnchantments(item)) {
			if (ench.getMaxLevel() != (short)1) lore.add(Messenger.formatString("&r&7" + ench.getDisplayName() + " &r&7" + NumUtil.toRoman(ench.getLevel(item))));
			else lore.add(Messenger.formatString("&r&7" + ench.getDisplayName()));
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
