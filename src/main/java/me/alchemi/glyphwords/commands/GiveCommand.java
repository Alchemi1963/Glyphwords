package me.alchemi.glyphwords.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.alchemi.al.Library;
import me.alchemi.al.objects.base.CommandBase;
import me.alchemi.glyphwords.Config.Options;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.enchantments.EnchantmentManager;
import me.alchemi.glyphwords.enchantments.EnchantmentManager.Enchantments;

public class GiveCommand extends CommandBase {

	public GiveCommand() {
		super(Glyph.getInstance(), "", "/giveenchant <player> <enchantment>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission(command.getPermission()) && sender instanceof Player) {
			Library.giveItemStack(EnchantmentManager.buildLore(Enchantments.XP_BOOST.getInstance().apply(new ItemStack(Options.BOOK_ITEM.asMaterial())
					, (short) 1)), (Player) sender);
			return true;
		}
		sendNoPermission(sender, command);
		return true;
	}
	
}
