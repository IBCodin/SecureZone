package me.ibcodin.plugins.securezone;

import junit.framework.TestCase;

import java.util.Random;

/**
 */
public class IntVectorTest extends TestCase {
    public void testIsInAABB() throws Exception {
        IntVector a = new IntVector(1,1,1);
        IntVector b = new IntVector(10,10,10);
        IntVector c = new IntVector(5,5,15);
        IntVector d = new IntVector(5,5,5);
        assertFalse(c.isInAABB(a, b));
        assertTrue(d.isInAABB(a,b));
    }

    public void testSetX() throws Exception {
        Random rgen = new Random();
        int xx = 0;

        IntVector iv = new IntVector(11,22,33);
        assertEquals(11, iv.getX());

        for (int ii = 0; ii < 10; ii++) {
            xx = rgen.nextInt();
            iv.setX(xx);
            assertTrue(xx == iv.getX());
        }
    }

    public void testSetY() throws Exception {
        Random rgen = new Random();
        int yy = 0;

        IntVector iv = new IntVector(11,22,33);
        assertEquals(22, iv.getY());

        for (int ii = 0; ii < 10; ii++) {
            yy = rgen.nextInt();
            iv.setY(yy);
            assertEquals(yy, iv.getY());
        }
    }

    public void testSetZ() throws Exception {
        Random rgen = new Random();
        int zz = 0;

        IntVector iv = new IntVector(11,22,33);
        assertEquals(33, iv.getZ());

        for (int ii = 0; ii < 10; ii++) {
            zz = rgen.nextInt();
            iv.setZ(zz);
            assertEquals(zz, iv.getZ());
        }

    }

    public void testEquals() throws Exception {
        // TODO: code testEquals() for IntVector
    }
}
