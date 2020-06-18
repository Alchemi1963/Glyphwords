package me.alchemi.glyphwords;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeUtil {

	public static ItemStack fixAttributes(ItemStack item) {
		item.setItemMeta(fixAttributes(item.getItemMeta(), item.getType()));
		return item;		
	}
	
	public static ItemMeta fixAttributes(ItemMeta meta, Material type) {
		switch(type) {
		case LEATHER_BOOTS:
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR, 1, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			break;
		case CHAINMAIL_BOOTS:
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR, 1, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			break;
		case IRON_BOOTS:
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR, 2, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			break;
		case GOLDEN_BOOTS:
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR, 1, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			break;
		case DIAMOND_BOOTS:
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR, 3, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			meta = addAttribute(meta, Attribute.GENERIC_ARMOR_TOUGHNESS, 2, Operation.ADD_NUMBER, EquipmentSlot.FEET);
			break;
			
		case WOODEN_SWORD:
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 1.6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 4, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			break;
		case STONE_SWORD:
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 1.6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 5, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			break; 
		case IRON_SWORD:
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 1.6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			break; 
		case GOLDEN_SWORD:
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 1.6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 4, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			break; 
		case DIAMOND_SWORD:	
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_SPEED, 1.6, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			meta = addAttribute(meta, Attribute.GENERIC_ATTACK_DAMAGE, 7, Operation.ADD_NUMBER, EquipmentSlot.HAND);
			break; 
			
		default:
			break;
		}
		return meta;
	}
	
	public static ItemMeta addAttribute(ItemMeta meta, Attribute attribute, double amount, Operation operation, EquipmentSlot slot) {
		meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.toString(), amount, operation, slot));
		return meta;
	}
	
	public static ItemStack addAttribute(ItemStack item, Attribute attribute, double amount, Operation operation, EquipmentSlot slot) {
		ItemMeta meta = item.getItemMeta();
		meta = addAttribute(meta, attribute, amount, operation, slot); 
		item.setItemMeta(meta);
		return item;
	}
	
}
