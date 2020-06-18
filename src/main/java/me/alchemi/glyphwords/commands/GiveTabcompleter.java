package me.alchemi.glyphwords.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.objects.base.TabCompleteBase;
import me.alchemi.glyphwords.EnchantmentManager.Enchantments;

public class GiveTabcompleter extends TabCompleteBase {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

		List<Object> list = new ArrayList<Object>();
		
		if (!(sender instanceof Player))
			return Arrays.asList("");
		
		if (args.length == 1) {
			list.addAll(Bukkit.getOnlinePlayers().parallelStream().map(Player::getName).collect(Collectors.toList()));
		} else if (args.length > 1) {
			list.addAll(Arrays.asList(Enchantments.values()));
		}		
		
		return returnSortSuggest(list, args);
	}

}
