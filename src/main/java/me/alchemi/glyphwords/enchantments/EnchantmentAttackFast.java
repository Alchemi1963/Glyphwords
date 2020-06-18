package me.alchemi.glyphwords.enchantments;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.alchemi.glyphwords.AttributeUtil;
import me.alchemi.glyphwords.Config.EnchantmentOptions;
import me.alchemi.glyphwords.util.EnchantUtil;

public class EnchantmentAttackFast extends Enchantment {

	public EnchantmentAttackFast() {
		super("attack_fast", EnchantmentType.MELEE_WEAPON);
		chance = EnchantmentOptions.ATTACK_FAST_CHANCE.asDouble();
		maxLevel = Short.parseShort(EnchantmentOptions.ATTACK_FAST_MAXLEVEL.asString());
	}
	
	@Override
	public ItemStack apply(ItemStack item, short level) {
		if (canEnchant(item) && type.getMaterials().contains(item.getType())) {
			ItemMeta meta = item.getItemMeta();
			double amount = level/EnchantmentOptions.ATTACK_FAST_DIVISION.asDouble();
			if (EnchantUtil.isItemEnchanted(item) && hasItem(item)) {
				meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
				
				short lvl = getLevel(item);
				if (lvl == level) {
					amount = (level + 1)/EnchantmentOptions.ATTACK_FAST_DIVISION.asDouble();
				} else if (lvl > level) {
					amount = lvl/EnchantmentOptions.ATTACK_FAST_DIVISION.asDouble();
				}
			}
			
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, 
					new AttributeModifier(UUID.randomUUID(), displayName, amount, Operation.ADD_SCALAR, EquipmentSlot.HAND));
			meta = AttributeUtil.fixAttributes(meta, item.getType());
			item.setItemMeta(meta);
		}
		return super.apply(item, level);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof LivingEntity) || e.getDamager().getType() != EntityType.PLAYER || !isPlayerHolding((Player)e.getDamager())) return;
		
		((LivingEntity)e.getEntity()).setNoDamageTicks(0);
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

	
	
}
