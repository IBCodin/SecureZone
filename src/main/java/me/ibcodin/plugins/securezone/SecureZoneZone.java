/**
 * 
 */
package me.ibcodin.plugins.securezone;

/**
 * Represents a single SecureZone
 * 
 * @author IBCodin
 */
public class SecureZoneZone {

	private String zoneName;
	private String worldName;
	private IntVector vmin;
	private IntVector vmax;
	private ZoneType ztype;
	private boolean valid;

	private void initdata(String zoneName, String worldName, ZoneType ztype, int x1,
			int y1, int z1, int x2, int y2, int z2) {
		this.zoneName = zoneName;
		this.worldName = worldName;
		this.ztype = ztype;
		vmin = new IntVector(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1,
				z2));
		vmax = new IntVector(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1,
				z2));
		valid = vmin != vmax;
	}

	/**
	 * Create a SecureZoneZone from parameters and vectors from a WorldEdit
	 * Region
	 * 
	 * @param name
	 *            Zone name
	 * @param worldName
	 *            World name
	 * @param ztype
	 *            The type of zone to create
	 * @param p1
	 *            The first vector (usually minimum)
	 * @param p2
	 *            The second vector (usually maximum)
	 */
	public SecureZoneZone(String name, String worldName, ZoneType ztype,
			com.sk89q.worldedit.Vector p1, com.sk89q.worldedit.Vector p2) {
		initdata(name, worldName, ztype, p1.getBlockX(), p1.getBlockY(),
				p1.getBlockZ(), p2.getBlockX(), p2.getBlockY(), p2.getBlockZ());
	}

	/**
	 * Create a SecureZoneZone from string data Primarily used to restore the
	 * zone from configuration
	 * 
	 * @param name
	 *            Zone name
	 * @param worldName
	 *            World name
	 * @param data
	 *            the string data to use
	 */
	public SecureZoneZone(String name, String worldName, String data) {
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		int x2 = 0;
		int y2 = 0;
		int z2 = 0;
		ZoneType iztype;

		final String fields[] = data.split(",");

		int fi = 0;
		if (fields.length == 7) {
			iztype = ZoneType.valueOf(fields[0].toUpperCase());
			fi = 1;
		} else {
			iztype = ZoneType.KEEPOUT;
		}
		if (fields.length == (6 + fi)) {
			x1 = Integer.parseInt(fields[fi]);
			y1 = Integer.parseInt(fields[fi + 1]);
			z1 = Integer.parseInt(fields[fi + 2]);
			x2 = Integer.parseInt(fields[fi + 3]);
			y2 = Integer.parseInt(fields[fi + 4]);
			z2 = Integer.parseInt(fields[fi + 5]);
		}
		initdata(name, worldName, iztype, x1, y1, z1, x2, y2, z2);
	}

	/**
	 * Provide a string representation of the zone, compatible with the data
	 * argument for SecureZoneZone(String name, String worldName, String data)
	 * 
	 * @return string representation
	 */
	public String dump() {
		return String.format("%s,%d,%d,%d,%d,%d,%d", ztype.toString(),
				vmin.getX(), vmin.getY(), vmin.getZ(), vmax.getX(),
				vmax.getY(), vmax.getZ());
	}

	/**
	 * See if this zone contains the point (vector)
	 * 
	 * @param test
	 *            the vector to test
	 * @return true if the point lies in the zone
	 */
	public boolean zoneContains(IntVector test) {
		return test.isInAABB(vmin, vmax);
	}

	/**
	 * Determine if the zone was 'filled' correctly Used by the configuration
	 * loading section to detect improperly formatted data
	 * 
	 * @return true if the data is valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return Zone name
	 */
	public String getName() {
		return zoneName;
	}

	/**
	 * @return World name
	 */
	public String getWorld() {
		return worldName;
	}

	/**
	 * @return Zone type
	 */
	public ZoneType getType() {
		return ztype;
	}

	/**
	 * Change the type of a zone
	 * 
	 * @param newType
	 *            The type to change to
	 */
	public void changeType(ZoneType newType) {
		ztype = newType;
	}

	/**
	 * @return the vector (point) at the center of the zone
	 */
	public IntVector visitloc() {
		return new IntVector((vmin.getX() + vmax.getX()) / 2,
				(vmin.getY() + vmax.getY()) / 2,
				(vmin.getZ() + vmax.getZ()) / 2);
	}
}
