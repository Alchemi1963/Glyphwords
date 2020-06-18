package me.alchemi.glyphwords.commands;

import java.util.Arrays;
import java.util.ListIterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.Library;
import me.alchemi.al.objects.base.CommandBase;
import me.alchemi.al.util.ItemUtil;
import me.alchemi.al.util.NumUtil;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.EnchantmentManager.Enchantments;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.enchantments.Enchantment;
import me.alchemi.glyphwords.util.EnchantUtil;

public class GiveCommand extends CommandBase {

	public GiveCommand() {
		super(Glyph.getInstance(), "", "/giveenchant <player> <enchantments>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission(command.getPermission())) {
			
			if (args.length >= 3) {
				
				Player receiver = Library.getPlayer(args[0]);
				if (receiver == null) return true;

				ItemStack book = new ItemStack(Options.BOOK_ITEM.asMaterial());
				ListIterator<String> iter = Arrays.asList(args).listIterator();
				while (iter.hasNext()) {
					String current = iter.next();
					if (Enchantments.hasKey(current)) {
						Enchantment enchant = Enchantments.getEnchantment(current);
						if (iter.hasNext() && NumUtil.isPrimitive(current = iter.next(), Short.class)) book = enchant.apply(book, Short.parseShort(current));
						else if (iter.hasNext()){
							book = enchant.apply(book, (short)1);
							iter.previous();
						}
					}
				}
				if (EnchantUtil.isItemEnchanted(book)) ItemUtil.giveItemStack(EnchantUtil.buildLore(book), receiver);
				return true;
				
			} else if (args.length == 2) {
				
				Player receiver = Library.getPlayer(args[0]);
				if (receiver == null) return true;
				
				if (Enchantments.hasKey(args[1])) {
					ItemStack book = Enchantments.getEnchantment(args[1]).createBook((short) 1);
					ItemUtil.giveItemStack(book, receiver);
					return true;
				}							
			}
			
			return true;
		}
		sendNoPermission(sender, command);
		return true;
	}
	
}
