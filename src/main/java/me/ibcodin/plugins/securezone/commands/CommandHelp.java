/**
 * 
 */
package me.ibcodin.plugins.securezone.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * CommandExecutor for /securezone (the help)
 * 
 * @author IBCodin
 */
public class CommandHelp implements org.bukkit.command.CommandExecutor {

	/**
	 */
	public CommandHelp() {
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		String fmt = ChatColor.RED + "%s" + ChatColor.WHITE + " - %s";

		sender.sendMessage(String.format(fmt, "/securezone",
				"Generate help list"));

		if (sender.hasPermission("securezone.list")) {
			sender.sendMessage(String.format(fmt, "/securezonelist [world]",
					"List the currently defined zones"));
		}

		if (sender.hasPermission("securezone.create")) {
			sender.sendMessage(String.format(fmt,
					"/securezonecreate [name] [zonetype]",
					"Create a new zone from WorldEdit selection"));
		}

		if (sender.hasPermission("securezone.delete")) {
			sender.sendMessage(String.format(fmt, "/securezonedelete [name]",
					"Delete an existing zone"));
		}

		if (sender.hasPermission("securezone.modify")) {
			sender.sendMessage(String.format(fmt,
					"/securezonemodify [name] [zonetype]",
					"Modify (change the type of) an existing zone"));
		}

		if (sender.hasPermission("securezone.visit")) {
			sender.sendMessage(String.format(fmt, "/securezonevisit [name]",
					"Travel to an existing zone"));
		}

		return true;
	}
}
