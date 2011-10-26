/**
 * 
 */
package me.ibcodin.plugins.securezone;

/**
 * This is an integer based 3D point. integers are used to speed calculations
 * and comparisons.
 * 
 * @author IBCodin
 */
public class IntVector {

	private int x, y, z;

	/**
	 * Empty constructor (all points are zero)
	 */
	public IntVector() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * Normal constructor
	 * 
	 * @param x
	 *            X value
	 * @param y
	 *            Y value
	 * @param z
	 *            Z value
	 */
	public IntVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Ask this point if it is contained with the AABB (Axis Aligned Bounding
	 * Box)
	 * 
	 * @param min
	 *            One corner of the box (The one with the lowest value for each
	 *            coordinate)
	 * @param max
	 *            Other corner (with the maximum values for each coordinate)
	 * @return true if point is within the box This is determined by comparing
	 *         each of the coordinates of this IntVector with the coordinates of
	 *         the corners. If all three coordinates of this IntVector lie
	 *         between the min and max, this point is 'in the box.'
	 */
	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	public boolean isInAABB(IntVector min, IntVector max) {
		return (x >= min.x) && (x <= max.x) && (y >= min.y) && (y <= max.y)
				&& (z >= min.z) && (z <= max.z);
	}

	/**
	 * @return the X value of the vector
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            set the X value of the vector
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the Y value of the vector
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            Set the Y value of the vector
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the Z value of the vector
	 */
	public int getZ() {
		return z;
	}

	/**
	 * @param z
	 *            Set the Z value of the vector
	 */
	public void setZ(int z) {
		this.z = z;
	}

    public boolean equals(IntVector other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }
}
