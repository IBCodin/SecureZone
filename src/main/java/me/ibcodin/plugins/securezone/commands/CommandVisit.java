package me.ibcodin.plugins.securezone.commands;

import me.ibcodin.plugins.securezone.IntVector;
import me.ibcodin.plugins.securezone.SecureZone;
import me.ibcodin.plugins.securezone.SecureZoneZone;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author IBCodin
 * 
 *         CommandExecutor for /securezonevisit
 */
public class CommandVisit implements CommandExecutor {

	protected SecureZone plugin = null;

	final protected String zoneIgnorePermission = new String(
			"securezone.ignorezones");
	final protected String zoneTPInPrefix = new String("securezone.tpin.");

	/**
	 * @param plugin
	 *            Reference to SecureZone plugin
	 */
	public CommandVisit(SecureZone plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		if (sender instanceof Player) {
			final Player psender = (Player) sender;

			if (args.length == 1) {
				final String zname = args[0];

				final SecureZoneZone zone = plugin.getZone(zname);

				if (zone != null) {
					if (sender.hasPermission(zoneIgnorePermission)
							|| sender.hasPermission(zoneTPInPrefix
									+ zone.getName())) {

						final IntVector vvec = zone.visitloc();

						final World world = plugin.getServer().getWorld(
								zone.getWorld());
						if (world != null) {

							// this block is at the 'center' of the zone
							Block tblock = world.getBlockAt(vvec.getX(),
									vvec.getY(), vvec.getZ());

							// move down until the block below us is solid
							Block nblock = tblock.getRelative(BlockFace.DOWN);
							int testid = nblock.getTypeId();
							while ((testid == 0)
									|| ((testid > 7) && (testid < 12))) {
								tblock = nblock;
								nblock = tblock.getRelative(BlockFace.DOWN);
								testid = nblock.getTypeId();
							}

							// move up until we have 2 clear blocks (to stand
							// in)
							nblock = tblock.getRelative(BlockFace.UP);
							while ((tblock.getTypeId() != 0)
									|| (nblock.getTypeId() != 0)) {
								tblock = nblock;
								nblock = tblock.getRelative(BlockFace.UP);
							}

							// tblock should be at the lowest point with 2
							// spaces over it (I hope)
							psender.teleport(tblock.getLocation());
						} else {
							sender.sendMessage("Zone: " + zone.getName()
									+ " World: " + zone.getWorld()
									+ ": World not loaded");
						}

					} else {
						sender.sendMessage("Zone: " + zone.getName()
								+ " : No permission to visit");
					}

				} else {
					sender.sendMessage("Unknown zone " + zname);
				}
			} else {
				sender.sendMessage("Zone names are a single word");
			}
		} else {
			sender.sendMessage("This command must be executed by an in-game player.");
		}
		return false;
	}
}
