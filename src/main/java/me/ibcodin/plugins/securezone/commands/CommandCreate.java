package me.ibcodin.plugins.securezone.commands;

import java.util.logging.Level;

import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;
import me.ibcodin.plugins.securezone.ZoneType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

/**
 * CommandExecutor for /securezonecreate
 * 
 * @author IBCodin
 */
public class CommandCreate implements CommandExecutor {

	private SecureZone plugin = null;
	private WorldEditAPI weapi = null;

	/**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandCreate(SecureZone plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		initWEAPI();

		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be an in-game player to run this command.");
			return false;
		}

		final Player player = (Player) sender;

		if ((args.length < 1) || (args.length > 2)) {
			sender.sendMessage("This command requires a one word zone name.");
			sender.sendMessage("You may optionally also supply a zone type.");
			return false;
		}

		final String zoneName = args[0];

		// Test for 'invalid' zone names that match command names/permissions
		for (final String rw : plugin.reservedWords) {
			if (zoneName.equalsIgnoreCase(rw)) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + zoneName
						+ "is a reserved name. Choose another name.");
				return false;
			}
		}

		if (plugin.isZone(zoneName)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Zone exists: " + zoneName
					+ ". Choose another name.");
			return false;
		}

		ZoneType zone_type = ZoneType.KEEPOUT;

		if (args.length > 1) {
			try {
				zone_type = ZoneType.valueOf(args[1].toUpperCase());
			} catch (final IllegalArgumentException ee) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE
						+ "Invalid zone type. Use one of: "
						+ ZoneType.getPrettyList());
				return false;
			}
		}

		final LocalSession session = weapi.getSession(player);
		final LocalWorld world = new BukkitWorld(player.getWorld());

		if (!session.isSelectionDefined(world)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE
					+ "You must have an active WorldEdit cubic selection to define the zone.");
			return false;
		}

		try {
			final Region selectedRegion = session.getSelection(world);

			final String worldName = player.getWorld().getName();

			plugin.addZone(new SecureZoneZone(zoneName, worldName, zone_type, selectedRegion
                    .getMinimumPoint(), selectedRegion.getMaximumPoint()));
			sender.sendMessage("Zone: " + zoneName
					+ " created from the WorldEdit selection");
			plugin.log(Level.INFO, sender.getName() + ": created zone: "
					+ zoneName);

		} catch (final IncompleteRegionException ee) {
			sender.sendMessage("The current region selection is incomplete.");
			return false;
		}

		return true;
	}

	private void initWEAPI() throws CommandException {
		if (weapi == null) {
			final Plugin weplug = plugin.getServer().getPluginManager()
					.getPlugin("WorldEdit");
			if (weplug == null) {
				throw new org.bukkit.command.CommandException(
						"WorldEdit plugin not found.");
			}
			if (!(weplug instanceof WorldEditPlugin)) {
				throw new org.bukkit.command.CommandException(
						"WorldEdit detection failed (report error).");
			}
			weapi = new WorldEditAPI((WorldEditPlugin) weplug);
		}
	}
}
