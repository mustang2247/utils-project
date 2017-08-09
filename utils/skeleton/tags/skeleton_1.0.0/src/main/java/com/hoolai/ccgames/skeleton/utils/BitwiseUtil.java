package com.hoolai.ccgames.skeleton.utils;

/**
 * Created by hoolai on 2016/7/18.
 */
public class BitwiseUtil {

    public static boolean isPow2( long x ) {
        return ( x & ( x - 1 ) ) == 0;
    }

    public static long lowBit( long x ) {
        return x & -x;
    }

    public static int lowBitIndex( long x ) {
        int rv = -1;
        while( x != 0 ) {
            ++rv;
            if( ( x & 1 ) == 1 ) break;
            x >>= 1;
        }
        return rv;
    }

    public static long nextPow2( long x ) {
        --x;
        x |= x >>> 1;
        x |= x >>> 2;
        x |= x >>> 4;
        x |= x >>> 8;
        x |= x >>> 16;
        x |= x >>> 32;
        return x + 1;
    }
}
