/**
 * 
 */
package me.ibcodin.plugins.securezone.commands;

import me.ibcodin.plugins.securezone.SecureZone;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author IBCodin
 * 
 *         CommandExecutor for /securezone (the help)
 */
public class CommandHelp implements org.bukkit.command.CommandExecutor {

	protected SecureZone plugin = null;

	/**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandHelp(SecureZone plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub

		String fmt = new String("%s - %s");
		if (sender instanceof Player) {
			fmt = new String(ChatColor.RED + "%s" + ChatColor.WHITE + " - %s");
		}

		sender.sendMessage(String.format(fmt, "/securezone",
				"Generate help list"));

		if (sender.hasPermission("securezone.list")) {
			sender.sendMessage(String.format(fmt, "/securezonelist",
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
