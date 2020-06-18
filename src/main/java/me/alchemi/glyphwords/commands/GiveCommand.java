package me.alchemi.glyphwords.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.base.CommandBase;
import me.alchemi.al.util.ItemUtil;
import me.alchemi.glyphwords.Glyph;
import me.alchemi.glyphwords.enchantments.EnchantmentManager.Enchantments;

public class GiveCommand extends CommandBase {

	public GiveCommand() {
		super(Glyph.getInstance(), "", "/giveenchant <player> <enchantment>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission(command.getPermission()) && sender instanceof Player) {
			ItemUtil.giveItemStack(Enchantments.INFINITY_CROSS.getInstance().createBook((short) 1), (Player) sender);
			return true;
		}
		sendNoPermission(sender, command);
		return true;
	}
	
}
