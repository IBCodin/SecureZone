/**
 * 
 */
package me.ibcodin.plugins.securezone;

/**
 * Enumeration of zone types
 * 
 * @author IBCodin
 */
public enum ZoneType {

	/**
	 * Zones that keep players out
	 */
	KEEPOUT("KeepOut"),
	/**
	 * Zones that keep players in
	 */
	KEEPIN("KeepIn");

	String pretty;

	ZoneType(String p) {
		pretty = p;
	}

	/**
	 * Get the 'pretty' (mixed-case) version of the enum
	 * 
	 * @return String with pretty version
	 */
	public String getPretty() {
		return pretty;
	}

	/**
	 * @return String with list of zone types
	 */
	public static String getPrettyList() {
		final StringBuilder bld = new StringBuilder();
		for (final ZoneType zti : values()) {
			bld.append(zti.pretty + ", ");
		}
		bld.setLength(bld.length() - 2);
		return bld.toString();
	}
};
