package me.ibcodin.plugins.securezone.commands;

import java.util.logging.Level;

import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * CommandExecutor for /securezonedelete
 * 
 * @author IBCodin
 */
public class CommandDelete implements CommandExecutor {

	private SecureZone plugin = null;

	/**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandDelete(SecureZone plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		boolean rval = false;

		if (args.length == 1) {
			final String zname = args[0];

			final SecureZoneZone zone = plugin.getZone(zname);

			if (zone != null) {
				plugin.delZone(zone);
				sender.sendMessage("Zone: " + zname + " deleted");
				plugin.log(Level.INFO, sender.getName() + ": deleted zone: "
						+ zname);
				rval = true;
			} else {
				sender.sendMessage("The zone name " + zname + " was not found.");
			}
		} else {
			sender.sendMessage("Zone names are a single word.");
		}

		return rval;
	}

}
