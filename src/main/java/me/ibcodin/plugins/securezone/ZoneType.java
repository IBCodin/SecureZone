/**
 * 
 */
package me.ibcodin.plugins.securezone;

/**
 * @author IBCodin
 * 
 *         Enumeration of zone types
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
		StringBuilder bld = new StringBuilder();
		for (ZoneType zti : values()) {
			bld.append(zti.pretty + ", ");
		}
		bld.setLength(bld.length() - 2);
		return bld.toString();
	}
};
