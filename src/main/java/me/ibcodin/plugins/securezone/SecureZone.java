package me.ibcodin.plugins.securezone;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.ibcodin.plugins.securezone.commands.CommandCreate;
import me.ibcodin.plugins.securezone.commands.CommandDelete;
import me.ibcodin.plugins.securezone.commands.CommandHelp;
import me.ibcodin.plugins.securezone.commands.CommandList;
import me.ibcodin.plugins.securezone.commands.CommandModify;
import me.ibcodin.plugins.securezone.commands.CommandVisit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Base class for the SecureZone plugin
 * 
 * @author IBCodin
 */
public class SecureZone extends JavaPlugin {

	private final HashMap<String, SecureZoneWorld> zoneWorldMap = new HashMap<String, SecureZoneWorld>();

	private final HashMap<String, SecureZoneZone> zoneMap = new HashMap<String, SecureZoneZone>();

	private static final Logger logger = Logger.getLogger("Minecraft.SecureZone");

	/**
	 * This list of words maps to 'already defined' permissions so these would
	 * make bad zone names
	 */
	final public String[] reservedWords = { "admin", "create", "delete",
			"list", "modify", "visit", "ignorezones" };

	/**
	 * Search to see if a world is defined
	 * 
	 * @param worldname
	 *            The name of the world to search for
	 * @return true if the world is found
	 */
	public boolean isWorld(String worldname) {
		return zoneWorldMap.get(worldname.toLowerCase()) != null;
	}

	/**
	 * Find (creating if necessary) a SecureZoneWorld by world name.
	 * 
	 * @param worldname
	 *            The name of the world to search for.
	 * @return The associated SecureZoneWorld.
	 */
	public SecureZoneWorld getZoneWorld(String worldname) {
		SecureZoneWorld rval = zoneWorldMap.get(worldname.toLowerCase());
		if (rval == null) {
			rval = new SecureZoneWorld();
			zoneWorldMap.put(worldname.toLowerCase(), rval);
		}
		return rval;
	}

	/**
	 * @return an unmodifyable set of world name strings
	 */
	public Set<String> getWorldList() {
		return Collections.unmodifiableSet(zoneWorldMap.keySet());
	}

	/**
	 * Search to see if a zone is defined
	 * 
	 * @param zonename
	 *            The zone name to search for.
	 * @return true if the zone is defined
	 */
	public boolean isZone(String zonename) {
		return zoneMap.get(zonename.toLowerCase()) != null;
	}

	/**
	 * Find a zone by zone name
	 * 
	 * @param zonename
	 *            The zone name to search for.
	 * @return SecureZoneZone the zone associated with the zone name. This may
	 *         return null if the zone is unknown.
	 */
	public SecureZoneZone getZone(String zonename) {
        return zoneMap.get(zonename.toLowerCase());
	}

	/**
	 * Add a 'new' zone to the zone list.
	 * 
	 * @param zone
	 *            The zone to add.
	 */
	public void addZone(SecureZoneZone zone) {
		final SecureZoneWorld world = getZoneWorld(zone.getWorld());
		zoneMap.put(zone.getName().toLowerCase(), zone);
		world.addZone(zone);

		// Update Configuration
		ConfigurationSection wsec = getConfig().getConfigurationSection(
				"worlds." + zone.getWorld());
		if (wsec == null) {
			wsec = getConfig().createSection("worlds." + zone.getWorld());
		}
		wsec.set(zone.getName(), zone.dump());

		saveConfig();
	}

	/**
	 * Remove a zone from the zone list
	 * 
	 * @param zone
	 *            Zone to be removed
	 */
	public void delZone(SecureZoneZone zone) {
		final SecureZoneWorld world = getZoneWorld(zone.getWorld());
		world.removeZone(zone);
		zoneMap.remove(zone.getName().toLowerCase());

		// getConfig().set("worlds." + zone.getWorld() + "." + zone.getName(),
		// null);
		if (world.isEmpty()) {
			zoneWorldMap.remove(zone.getWorld());
			// getConfig().set("worlds." + zone.getWorld(), null);
		}

		// TODO: Replace the hack below with the code above when the
		// implementation to delete data is available.

		final ConfigurationSection wsec = getConfig().createSection(
				"worlds." + zone.getWorld());
		for (final SecureZoneZone izone : world.getList()) {
			wsec.set(izone.getName(), izone.dump());
		}
		saveConfig();
	}

	/**
	 * Update the configuration with the modified zone
	 * 
	 * @param zone
	 *            the zone which has been modified
	 */
	public void updateZone(SecureZoneZone zone) {
		// Not testing for world configuration -- this should be an existing
		// zone
		getConfig().set("worlds." + zone.getWorld() + "." + zone.getName(),
				zone.dump());
	}

	/**
	 * Log information to the server from or about this plugin
	 * 
	 * @param level
	 *            The logging level for the message. Use Level.INFO for most
	 *            data Use Level.WARNING for problem information Use
	 *            Level.SEVERE for critical, fix-now type information
	 * @param message
	 *            The message to log
	 */
	public void log(Level level, String message) {
		logger.log(level, "[SecureZone] {0}", message);
	}

	// /**
	// * Return the SecureZoneWorld for a given location
	// * @param test
	// * @return
	// */
	// protected SecureZoneWorld getWorld(Location test) {
	// return getZoneWorld(test.getWorld().getName());
	// }

	public void onDisable() {
		// The config on disk should be current

		// We'll just flush the lists on our way out.
		zoneWorldMap.clear();
		zoneMap.clear();

		System.out.println("[" + this + "] is now disabled!");
	}

	@SuppressWarnings("ResultOfObjectAllocationIgnored")
	public void onEnable() {
		log(Level.INFO, "Starting onEnable");

		final FileConfiguration config;
        config = getConfig();
        assert config != null;

        final ConfigurationSection worlds;
        worlds = config.getConfigurationSection("worlds");
        if (worlds == null) {
            log(Level.INFO, "No Zones Defined");
        } else {
            final Set<String> wkeys;
            wkeys = worlds.getKeys(false);      // false- don't get sub keys
            if (wkeys.isEmpty()) {
                log(Level.INFO, "No Zones Defined");
            }
            for (String worldname : wkeys) {
                log(Level.INFO, worldname + ": Loading zones");

                final ConfigurationSection aworld;
                aworld = worlds.getConfigurationSection(worldname);

                for (String zonename : aworld.getKeys(false)) {
                    final String zonedata;
                    zonedata = aworld.getString(zonename);
                    final SecureZoneZone szone;
                    szone = new SecureZoneZone(zonename, worldname, zonedata);
                    if (szone.isValid()) {
                        log(Level.INFO, zonename + ": loaded");
                        // Don't call this.addZone(zone) as it would try to
                        // update the config we're parsing
                        final SecureZoneWorld world;
                        world = getZoneWorld(szone.getWorld());
                        zoneMap.put(szone.getName().toLowerCase(), szone);
                        world.addZone(szone);
                    } else {
                        log(Level.WARNING, zonename + ": improperly formed");
                    }
                }
            }
        }

        config.options().copyDefaults(true);

		saveConfig();

		getCommand("securezone").setExecutor(new CommandHelp());
		getCommand("securezonecreate").setExecutor(new CommandCreate(this));
		getCommand("securezonedelete").setExecutor(new CommandDelete(this));
		getCommand("securezonelist").setExecutor(new CommandList(this));
		getCommand("securezonemodify").setExecutor(new CommandModify(this));
		getCommand("securezonevisit").setExecutor(new CommandVisit(this));
		
		new SecureZonePlayerListener(this);

		System.out.println("[" + this + "] is now enabled!");
	}
}
