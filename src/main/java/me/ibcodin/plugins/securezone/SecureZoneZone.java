/**
 * 
 */
package me.ibcodin.plugins.securezone;

/**
 * @author IBCodin Represents a single SecureZone
 */
public class SecureZoneZone {

	private String zname;
	private String wname;
	private IntVector vmin;
	private IntVector vmax;
	private ZoneType ztype;
	private boolean valid;

	private void initdata(String zname, String wname, ZoneType ztype, int x1,
			int y1, int z1, int x2, int y2, int z2) {
		this.zname = zname;
		this.wname = wname;
		this.ztype = ztype;
		vmin = new IntVector(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1,
				z2));
		vmax = new IntVector(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1,
				z2));
		valid = vmin != vmax;
	}

	// public SecureZoneZone(String name, String wname, double x1, double y1,
	// double z1, double x2, double y2, double z2) {
	// initdata(name, wname, x1, y1, z1, x2, y2, z2);
	// }

	// /**
	// * @param name
	// * @param wname
	// * @param ztype
	// * @param p1
	// * @param p2
	// */
	// public SecureZoneZone(String name, String wname, ZoneType ztype, Vector
	// p1,
	// Vector p2) {
	// initdata(name, wname, ztype, p1.getBlockX(), p1.getBlockY(),
	// p1.getBlockZ(), p2.getBlockX(), p2.getBlockY(), p2.getBlockZ());
	// }

	/**
	 * Create a SecureZoneZone from parameters and vectors from a WorldEdit
	 * Region
	 * 
	 * @param name
	 *            Zone name
	 * @param wname
	 *            World name
	 * @param ztype
	 *            The type of zone to create
	 * @param p1
	 *            The first vector (usually minimum)
	 * @param p2
	 *            The second vector (usually maximum)
	 */
	public SecureZoneZone(String name, String wname, ZoneType ztype,
			com.sk89q.worldedit.Vector p1, com.sk89q.worldedit.Vector p2) {
		initdata(name, wname, ztype, p1.getBlockX(), p1.getBlockY(),
				p1.getBlockZ(), p2.getBlockX(), p2.getBlockY(), p2.getBlockZ());
	}

	/**
	 * Create a SecureZoneZone from string data Primarily used to restore the
	 * zone from configuration
	 * 
	 * @param name
	 *            Zone name
	 * @param wname
	 *            World name
	 * @param data
	 *            the string data to use
	 */
	public SecureZoneZone(String name, String wname, String data) {
		int x1 = 0;
		int y1 = 0;
		int z1 = 0;
		int x2 = 0;
		int y2 = 0;
		int z2 = 0;
		ZoneType ztype;

		final String fields[] = data.split(",");

		int fi = 0;
		if (fields.length == 7) {
			ztype = ZoneType.valueOf(fields[0].toUpperCase());
			fi = 1;
		} else {
			ztype = ZoneType.KEEPOUT;
		}
		if (fields.length == (6 + fi)) {
			x1 = Integer.parseInt(fields[fi]);
			y1 = Integer.parseInt(fields[fi + 1]);
			z1 = Integer.parseInt(fields[fi + 2]);
			x2 = Integer.parseInt(fields[fi + 3]);
			y2 = Integer.parseInt(fields[fi + 4]);
			z2 = Integer.parseInt(fields[fi + 5]);
		}
		initdata(name, wname, ztype, x1, y1, z1, x2, y2, z2);
	}

	/**
	 * Provide a string representation of the zone, compatible with the data
	 * argument for SecureZoneZone(String name, String wname, String data)
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
		return zname;
	}

	/**
	 * @return World name
	 */
	public String getWorld() {
		return wname;
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
	 * @param newtype
	 *            The type to change to
	 */
	public void changeType(ZoneType newtype) {
		ztype = newtype;
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
