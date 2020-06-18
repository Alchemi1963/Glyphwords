package me.alchemi.glyphwords.enchantments;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.alchemi.glyphwords.AttributeUtil;
import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.util.EnchantUtil;

public class EnchantmentExtraSpeed extends Enchantment {

	public EnchantmentExtraSpeed() {
		super("extra_speed", EnchantmentType.BOOTS);
		chance = EnchantmentOptions.EXTRA_SPEED_CHANCE.asDouble();
		maxLevel = Short.parseShort(EnchantmentOptions.EXTRA_SPEED_MAXLEVEL.asString());
	}
	
	@Override
	public ItemStack apply(ItemStack item, short level) {
		System.out.println(item.getType());
		if (canEnchant(item) && type.getMaterials().contains(item.getType())) {
			
			ItemMeta meta = item.getItemMeta();
			double amount = level/EnchantmentOptions.EXTRA_SPEED_DIVISION.asDouble();
			if (EnchantUtil.isItemEnchanted(item) && hasItem(item)) {
				meta.removeAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED);
				
				short lvl = getLevel(item);
				if (lvl == level) {
					amount = (level + 1)/EnchantmentOptions.EXTRA_SPEED_DIVISION.asDouble();
				} else if (lvl > level) {
					amount = lvl/EnchantmentOptions.EXTRA_SPEED_DIVISION.asDouble();
				}
			}
			meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, 
					new AttributeModifier(UUID.randomUUID(), displayName, amount, Operation.ADD_SCALAR, EquipmentSlot.FEET));
			meta = AttributeUtil.fixAttributes(meta, item.getType());
			item.setItemMeta(meta);
		}
		return super.apply(item, level);
	}
	
	@Override
	public boolean hasConflicting(ItemStack item) {
		return item.containsEnchantment(org.bukkit.enchantments.Enchantment.FROST_WALKER);
	}

	@Override
	public boolean isConflicting(Enchantment enchant) {
		return false;
	}

	@Override
	public boolean isConflicting(org.bukkit.enchantments.Enchantment enchant) {
		return enchant == org.bukkit.enchantments.Enchantment.FROST_WALKER;
	}

}
