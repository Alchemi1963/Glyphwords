package me.alchemi.glyphwords.enchantments;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Sets;

import me.alchemi.glyphwords.Config.Options;

public enum EnchantmentType {

	TOOL(Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, 
			Material.STONE_AXE, Material.STONE_HOE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, 
			Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE, Material.IRON_SHOVEL,
			Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL,
			Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL,
			Material.FISHING_ROD, Material.FLINT_AND_STEEL, Material.SHEARS),
	WEAPON(Material.WOODEN_AXE, Material.WOODEN_SWORD,
			Material.STONE_AXE, Material.STONE_SWORD,
			Material.IRON_AXE, Material.IRON_SWORD,
			Material.DIAMOND_AXE, Material.DIAMOND_SWORD,
			Material.BOW, Material.CROSSBOW, Material.TRIDENT),
	MELEE_WEAPON(Material.WOODEN_AXE, Material.WOODEN_SWORD,
			Material.STONE_AXE, Material.STONE_SWORD,
			Material.IRON_AXE, Material.IRON_SWORD,
			Material.DIAMOND_AXE, Material.DIAMOND_SWORD),
	RANGED_WEAPON(Material.BOW, Material.CROSSBOW, Material.TRIDENT),
	ARMOUR(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
		Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
		Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
		Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
		Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.TURTLE_HELMET),
	HELMET(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.TURTLE_HELMET),
	CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE),
	LEGGINGS(Material.LEATHER_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS),
	BOOTS(Material.LEATHER_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS),
	CUSTOM;
	
	private Set<Material> materials;
	
	private EnchantmentType(Material... materials) {
		this.materials = Sets.newHashSet(materials);
	}
	
	public Set<Material> getMaterials() {
		return materials;
	}
	
	public boolean testItem(ItemStack item) {
		return item.getType().isItem() && !item.getType().isBlock() && (materials.contains(item.getType()) || item.getType() == Options.BOOK_ITEM.asMaterial());
	}
	
}
