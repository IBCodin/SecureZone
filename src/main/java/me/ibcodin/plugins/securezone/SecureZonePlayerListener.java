/**
 * 
 */
package me.ibcodin.plugins.securezone;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listener for player-related events
 * 
 * @author IBCodin
 */
public class SecureZonePlayerListener implements Listener {

	private final SecureZone plugin;

	final private String zoneIgnorePermission = "securezone.ignorezones";
	final private String zonePrefix = "securezone.";

	// Additional permissions:

	/**
	 * @param plugin
	 *            Reference to the SecureZone plugin
	 */
	public SecureZonePlayerListener(SecureZone plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
	}

	/**
	 * Test player movement against currently defined zones
	 * 
	 * @param ply
	 *            Player attempting the move
	 * @param from
	 *            location the player is moving from
	 * @param to
	 *            location the player is moving to
	 * @return true if movement should be prevented
	 */
	private boolean testZones(Player ply, Location from, Location to) {
		boolean rval = false;

		final String fromWorld;
        final String toWorld;

        fromWorld = from.getWorld().getName();
        toWorld = to.getWorld().getName();

        final IntVector fromVector;
        final IntVector toVector;

        fromVector = new IntVector(from.getBlockX(), from.getBlockY(), from.getBlockZ());
        toVector = new IntVector(to.getBlockX(), to.getBlockY(), to.getBlockZ());

        // Vector velocity = ply.getVelocity();
		// ply.setVelocity(velocity);

		if (!fromWorld.equals(toWorld)) {
			// the from and to worlds are different
			rval = testFromWorld(ply, fromWorld, fromVector)
					|| testToWorld(ply, toWorld, toVector);
		} else if (fromVector != toVector) {
			// we moved at least one block physically
			rval = testOneWold(ply, fromWorld, fromVector, toVector);
		}

		return rval;
	}

	/**
	 * Test the zones on the 'to' world
	 * 
	 * @param player
	 *            Player making the move
	 * @param worldName
	 *            name of the world to test in
	 * @param toVector
	 *            to location
	 * @return true if the move should be cancelled
	 */
	private boolean testToWorld(Player player, final String worldName,
			final IntVector toVector) {
		boolean rval = false;
		// check the tos
		// get the world
		if (plugin.isWorld(worldName)) {
			for (final SecureZoneZone zone : plugin.getZoneWorld(worldName)
					.getList()) {
				if ((zone.getType() == ZoneType.KEEPOUT) 
						&& (zone.zoneContains(toVector))) {
					final String perm = zonePrefix.concat(zone.getName());
					if (!player.hasPermission(perm)) {
						player.sendMessage(ChatColor.LIGHT_PURPLE
								+ "Zone Entry Not Authorized");
						rval = true;
						break;
					}
				}
			}
		}
		return rval;
	}

	/**
	 * Test the zones on the 'from' world
	 * 
	 * @param player
	 *            Player making the move
	 * @param worldName
	 *            name of the world to test in
	 * @param fromVector
	 *            from location
	 * @return true if the move should be cancelled
	 */
	private boolean testFromWorld(Player player, final String worldName,
			final IntVector fromVector) {
		boolean rval = false;
		// check the zones in the world we are moving from
		// get the world
		if (plugin.isWorld(worldName)) {
			for (final SecureZoneZone zone : plugin.getZoneWorld(worldName)
					.getList()) {

				if ((zone.getType() == ZoneType.KEEPIN) 
						&& (zone.zoneContains(fromVector))) {
					final String perm = zonePrefix.concat(zone.getName());
					if (!player.hasPermission(perm)) {
						player.sendMessage(ChatColor.LIGHT_PURPLE
								+ "Zone Exit Not Authorized");
						rval = true;
						break;
					}
				}
			}
		}
		return rval;
	}

	/**
	 * Test the zones on the world where the 'from' and 'to' are located
	 * 
	 * @param player
	 *            Player making the move
	 * @param worldName
	 *            name of the world to test in
	 * @param fromVector
	 *            from location
	 * @param toVector
	 *            to location
	 * @return true if the move should be cancelled
	 */
	private boolean testOneWold(Player player, final String worldName,
			final IntVector fromVector, final IntVector toVector) {
		boolean rval = false;
		// get the world
		if (plugin.isWorld(worldName)) {

			for (final SecureZoneZone zone : plugin.getZoneWorld(worldName)
					.getList()) {
				final boolean fin = zone.zoneContains(fromVector);
				final boolean tin = zone.zoneContains(toVector);

				if (fin && !tin && (zone.getType() == ZoneType.KEEPIN)) {
					// from is in, to is not -- we're moving out
					final String perm = zonePrefix.concat(zone.getName());

					if (!player.hasPermission(perm)) {
						// plugin.log(Level.INFO,ply.getDisplayName() +
						// ": Failed: " + perm);
						player.sendMessage(ChatColor.LIGHT_PURPLE
								+ "Zone Exit Not Authorized");
						rval = true;
						break;
					}
				} else if (tin && !fin && (zone.getType() == ZoneType.KEEPOUT)) {
					// to is in, from is not -- we're moving in
					final String perm = zonePrefix.concat(zone.getName());

					if (!player.hasPermission(perm)) {
						// plugin.log(Level.INFO,ply.getDisplayName() +
						// ": Failed: " + perm);
						player.sendMessage(ChatColor.LIGHT_PURPLE
								+ "Zone Entry Not Authorized");
						rval = true;
						break;
					}
				}
			}
		}
		return rval;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if ((!event.getPlayer().hasPermission(zoneIgnorePermission)) 
				&& (testZones(event.getPlayer(), event.getFrom(), event.getTo()))) {
			// For move events -- relocate them to the center of the block
			// they came from
			final Location newTarget;
            newTarget = event.getFrom();
            newTarget.setX(newTarget.getBlockX() + 0.5);
			newTarget.setY(newTarget.getBlockY());
			newTarget.setZ(newTarget.getBlockZ() + 0.5);
			event.setTo(newTarget);
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if ((!event.getPlayer().hasPermission(zoneIgnorePermission)) 
				&& (testZones(event.getPlayer(), event.getFrom(), event.getTo()))) {
			// For teleport events, cancel and leave them where they were
			event.setCancelled(true);
		}
	}
}
