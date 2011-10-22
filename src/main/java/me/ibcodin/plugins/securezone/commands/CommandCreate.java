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

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		initWEAPI();

		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be an in-game player to run this command.");
			return false;
		}

		final Player psender = (Player) sender;

		if ((args.length < 1) || (args.length > 2)) {
			sender.sendMessage("This command requires a one word zone name.");
			sender.sendMessage("You may optionally also supply a zone type.");
			return false;
		}

		final String zname = args[0];

		// Test for 'invalid' zone names that match command names/permissions
		for (final String rw : plugin.reservedWords) {
			if (zname.equalsIgnoreCase(rw)) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE + zname
						+ "is a reserved name. Choose another name.");
				return false;
			}
		}

		if (plugin.isZone(zname)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Zone exists: " + zname
					+ ". Choose another name.");
			return false;
		}

		ZoneType ztype = ZoneType.KEEPOUT;

		if (args.length > 1) {
			try {
				ztype = ZoneType.valueOf(args[1].toUpperCase());
			} catch (final IllegalArgumentException ee) {
				sender.sendMessage(ChatColor.LIGHT_PURPLE
						+ "Invalid zone type. Use one of: "
						+ ZoneType.getPrettyList());
				return false;
			}
		}

		final LocalSession session = weapi.getSession(psender);
		final LocalWorld world = new BukkitWorld(psender.getWorld());

		if (!session.isSelectionDefined(world)) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE
					+ "You must have an active WorldEdit cubic selection to define the zone.");
			return false;
		}

		try {
			final Region selregion = session.getSelection(world);

			final String wname = psender.getWorld().getName();

			plugin.addZone(new SecureZoneZone(zname, wname, ztype, selregion
					.getMinimumPoint(), selregion.getMaximumPoint()));
			sender.sendMessage("Zone: " + zname
					+ " created from the WorldEdit selection");
			plugin.log(Level.INFO, sender.getName() + ": created zone: "
					+ zname);

		} catch (final IncompleteRegionException ee) {
			sender.sendMessage("The current region selection is incomplete.");
			return false;
		}

		return true;
	}

	/**
	 * @throws CommandException
	 */
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
