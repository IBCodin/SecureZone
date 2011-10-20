package me.ibcodin.plugins.securezone.commands;

import java.util.Set;
import java.util.logging.Level;

import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;
import me.ibcodin.plugins.securezone.SecureZoneZone.ZoneType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author IBCodin
 * 
 *         CommandExecutor for /securezonelist
 */
public class CommandList implements CommandExecutor {

	protected SecureZone plugin = null;

	/**
	 * Implements the command /securezonelist
	 * 
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandList(SecureZone plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 0) {
			final Set<String> worlds = plugin.getWorldList();
			for (final String worldname : worlds) {
				listWorld(sender, worldname);
			}
		} else {
			final String worldname = args[0];
			if (plugin.isWorld(worldname)) {
				listWorld(sender, worldname);
			} else {
				sender.sendMessage("The world " + worldname
						+ " is unknown or has no zones");
			}
		}

		return true;
	}

	/**
	 * Generate a listing of the zones in a world
	 * 
	 * @param sender
	 *            the requester of the list
	 * @param worldname
	 *            the name of the world to list
	 */
	private void listWorld(CommandSender sender, String worldname) {
		sender.sendMessage(ChatColor.RED + worldname + ":");

		final StringBuilder kizones = new StringBuilder();
		final StringBuilder kozones = new StringBuilder();

		for (final SecureZoneZone zone : plugin.getZoneWorld(worldname)
				.getList()) {
			if (zone.getType() == ZoneType.KEEPIN) {
				kizones.append(zone.getName() + ", ");
			} else if (zone.getType() == ZoneType.KEEPOUT) {
				kozones.append(zone.getName() + ", ");
			} else {
				plugin.log(Level.WARNING,
						"Unknown ZoneType for Zone " + zone.getName());
			}
		}
		if (kizones.length() > 0) {
			kizones.setLength(kizones.length() - 2);
			sender.sendMessage(ChatColor.WHITE + "KeepIn: "
					+ kizones.toString());
		}
		if (kozones.length() > 0) {
			kozones.setLength(kozones.length() - 2);
			sender.sendMessage(ChatColor.WHITE + "KeepOut: "
					+ kozones.toString());
		}
	}

}
