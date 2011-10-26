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
 * CommandExecutor for /securezonemodify
 * 
 * @author IBCodin
 */
public class CommandModify implements CommandExecutor {

	private SecureZone plugin = null;

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
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		boolean rval = false;

		if (args.length == 2) {
			final String zoneName = args[0];

			if (plugin.isZone(zoneName)) {
				try {
					final ZoneType zone_type = ZoneType.valueOf(args[1]
							.toUpperCase());

					final SecureZoneZone zone = plugin.getZone(zoneName);
					zone.changeType(zone_type);
					plugin.updateZone(zone);

					sender.sendMessage("Zone: " + zoneName + " changed to "
                            + zone_type.toString());
					plugin.log(Level.INFO,
                            sender.getName() + ": changed zone: " + zoneName
                                    + " to: " + zone_type.toString());
					rval = true;

				} catch (final IllegalArgumentException ee) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE
							+ "Invalid zone type. Use one of: "
							+ ZoneType.getPrettyList());
				}
			} else {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "Unknown zone "
						+ zoneName);
			}
		} else {
			sender.sendMessage(ChatColor.LIGHT_PURPLE
					+ "You must specify a zone to modify and the new zone type");
		}

		return rval;
	}
}
