/**
 * 
 */
package me.ibcodin.plugins.securezone.commands;

import java.util.logging.Level;

import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;
import me.ibcodin.plugins.securezone.ZoneType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author IBCodin
 * 
 *         CommandExecutor for /securezonemodify
 */
public class CommandModify implements CommandExecutor {

	protected SecureZone plugin = null;

	/**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandModify(SecureZone plugin) {
		this.plugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		boolean rval = false;

		if (args.length == 2) {
			final String zonename = args[0];

			if (plugin.isZone(zonename)) {
				try {
					final ZoneType ztype = ZoneType.valueOf(args[1]
							.toUpperCase());

					final SecureZoneZone zone = plugin.getZone(zonename);
					zone.changeType(ztype);
					plugin.updateZone(zone);

					sender.sendMessage("Zone: " + zonename + " changed to "
							+ ztype.toString());
					plugin.log(Level.INFO,
							sender.getName() + ": changed zone: " + zonename
									+ " to: " + ztype.toString());
					rval = true;

				} catch (final IllegalArgumentException ee) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + 
							"Invalid zone type. Use one of: " + 
							ZoneType.getPrettyList());
				}
			} else {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Unknown zone "
						+ zonename);
			}
		} else {
			sender.sendMessage(ChatColor.LIGHT_PURPLE
					+ "You must specify a zone to modify and the new zone type");
		}

		return rval;
	}
}
