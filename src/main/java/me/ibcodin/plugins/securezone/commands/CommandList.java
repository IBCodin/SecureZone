package me.ibcodin.plugins.securezone.commands;

import java.util.EnumMap;
import java.util.Set;

import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;
import me.ibcodin.plugins.securezone.ZoneType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * CommandExecutor for /securezonelist
 * 
 * @author IBCodin
 */
public class CommandList implements CommandExecutor {

	private SecureZone plugin = null;

	/**
	 * Implements the command /securezonelist
	 * 
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandList(SecureZone plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 0) {
			final Set<String> worlds = plugin.getWorldList();
			for (final String worldName : worlds) {
				listWorld(sender, worldName);
			}
		} else {
			final String worldName = args[0];
			if (plugin.isWorld(worldName)) {
				listWorld(sender, worldName);
			} else {
				sender.sendMessage("The world " + worldName
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

		final ZoneType[] zone_types = ZoneType.values();
		final EnumMap<ZoneType, StringBuilder> zones = new EnumMap<ZoneType, StringBuilder>(
				ZoneType.class);

		for (final ZoneType zti : zone_types) {
			zones.put(zti, new StringBuilder());
		}

		for (final SecureZoneZone zone : plugin.getZoneWorld(worldname)
				.getList()) {
			zones.get(zone.getType()).append(zone.getName()).append(", ");
		}

		for (final ZoneType zti : zone_types) {
			final StringBuilder sti = zones.get(zti);
			if (sti.length() > 2) {
				sti.setLength(sti.length() - 2);
				sender.sendMessage(zti.getPretty() + ": " + sti.toString());
			}
		}
	}
}
