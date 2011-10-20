/**
 * 
 */
package me.ibcodin.plugins.securezone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author IBCodin
 * Container to hold all of the zones for a specific world
 */
public class SecureZoneWorld {
	/**
	 * Contains the list of zones that are defined in this world
	 */
	private final List<SecureZoneZone> zoneList = new ArrayList<SecureZoneZone>();

	/**
	 * @param zone
	 *            the zone to add to this world's list
	 */
	public void addZone(SecureZoneZone zone) {
		zoneList.add(zone);
	}

	/**
	 * @param zone
	 *            the zone to remove (by reference) from this world's list
	 */
	public void removeZone(SecureZoneZone zone) {
		zoneList.remove(zone);
	}

	/**
	 * Get the list of zones for this world
	 * 
	 * @return an unmodifiable copy of the list
	 */
	public List<SecureZoneZone> getList() {
		return Collections.unmodifiableList(zoneList);
	}

	/**
	 * See if the world has no zones
	 * 
	 * @return true if there are no zones for this world
	 */
	public boolean isEmpty() {
		return zoneList.isEmpty();
	}
}
