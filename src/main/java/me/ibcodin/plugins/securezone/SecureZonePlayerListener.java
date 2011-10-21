/**
 * 
 */
package me.ibcodin.plugins.securezone;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author IBCodin
 * 
 *         Listener for player-related events
 */
public class SecureZonePlayerListener extends PlayerListener {

	private final SecureZone plugin;

	final protected String zoneIgnorePermission = new String(
			"securezone.ignorezones");
	final protected String zonePrefix = new String("securezone.");

	// Additional permissions:

	/**
	 * @param plugin
	 *            Reference to the SecureZone plugin
	 */
	public SecureZonePlayerListener(SecureZone plugin) {
		this.plugin = plugin;

		// plugin.log(Level.INFO, "Registering for PLAYER_MOVE");
		plugin.getServer().getPluginManager()
				.registerEvent(Type.PLAYER_MOVE, this, Priority.Normal, plugin);
		// plugin.log(Level.INFO, "Registering for PLAYER_TELEPORT");
		plugin.getServer()
				.getPluginManager()
				.registerEvent(Type.PLAYER_TELEPORT, this, Priority.Normal,
						plugin);
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

		final String fworld = from.getWorld().getName();
		final String tworld = to.getWorld().getName();

		final IntVector fvec = new IntVector(from.getBlockX(),
				from.getBlockY(), from.getBlockZ());
		final IntVector tvec = new IntVector(to.getBlockX(), to.getBlockY(),
				to.getBlockZ());

		if (fworld != tworld) {
			// the from and to worlds are different
			rval = testFromWorld(ply, fworld, fvec)
					|| testToWorld(ply, tworld, tvec);
		} else if (fvec != tvec) {
			// we moved at least one block physically
			rval = testOneWold(ply, fworld, fvec, tvec);
		}

		return rval;
	}

	/**
	 * Test the zones on the 'to' world
	 * 
	 * @param player
	 *            Player making the move
	 * @param worldname
	 *            name of the world to test in
	 * @param tvec
	 *            to location
	 * @return true if the move should be cancelled
	 */
	private boolean testToWorld(Player player, final String worldname,
			final IntVector tvec) {
		boolean rval = false;
		// check the tos
		// get the world
		if (plugin.isWorld(worldname)) {
			for (final SecureZoneZone zone : plugin.getZoneWorld(worldname)
					.getList()) {
				if (zone.getType() == ZoneType.KEEPOUT) {
					if (zone.zoneContains(tvec)) {
						final String perm = zonePrefix.concat(zone.getName());
						if (!player.hasPermission(perm)) {
							rval = true;
							break;
						}
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
	 * @param worldname
	 *            name of the world to test in
	 * @param fvec
	 *            from location
	 * @return true if the move should be cancelled
	 */
	private boolean testFromWorld(Player player, final String worldname,
			final IntVector fvec) {
		boolean rval = false;
		// check the froms
		// get the world
		if (plugin.isWorld(worldname)) {
			for (final SecureZoneZone zone : plugin.getZoneWorld(worldname)
					.getList()) {

				if (zone.getType() == ZoneType.KEEPIN) {
					if (zone.zoneContains(fvec)) {
						final String perm = zonePrefix.concat(zone.getName());
						if (!player.hasPermission(perm)) {
							rval = true;
							break;
						}
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
	 * @param worldname
	 *            name of the world to test in
	 * @param fvec
	 *            from location
	 * @param tvec
	 *            to location
	 * @return true if the move should be cancelled
	 */
	private boolean testOneWold(Player player, final String worldname,
			final IntVector fvec, final IntVector tvec) {
		boolean rval = false;
		// get the world
		if (plugin.isWorld(worldname)) {

			for (final SecureZoneZone zone : plugin.getZoneWorld(worldname)
					.getList()) {
				final boolean fin = zone.zoneContains(fvec);
				final boolean tin = zone.zoneContains(tvec);

				if (fin && !tin && (zone.getType() == ZoneType.KEEPIN)) {
					// from is in, to is not -- we're moving out
					final String perm = zonePrefix.concat(zone.getName());

					if (!player.hasPermission(perm)) {
						// plugin.log(Level.INFO,ply.getDisplayName() +
						// ": Failed: " + perm);
						rval = true;
						break;
					}
				} else if (tin && !fin && (zone.getType() == ZoneType.KEEPOUT)) {
					// to is in, from is not -- we're moving in
					final String perm = zonePrefix.concat(zone.getName());

					if (!player.hasPermission(perm)) {
						// plugin.log(Level.INFO,ply.getDisplayName() +
						// ": Failed: " + perm);
						rval = true;
						break;
					}
				}
			}
		}
		return rval;
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!event.getPlayer().hasPermission(zoneIgnorePermission)) {
			if (testZones(event.getPlayer(), event.getFrom(), event.getTo())) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (!event.getPlayer().hasPermission(zoneIgnorePermission)) {
			if (testZones(event.getPlayer(), event.getFrom(), event.getTo())) {
				event.setCancelled(true);
			}
		}
	}
}
