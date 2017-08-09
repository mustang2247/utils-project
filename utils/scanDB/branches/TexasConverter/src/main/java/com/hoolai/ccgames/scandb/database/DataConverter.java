/**
 * Author: guanxin
 * Date: 2015-07-27
 */

package com.hoolai.ccgames.scandb.database;

public class DataConverter {

	public static int writeBytes( int x, byte[] b, int pos ) {

		b[pos++] = (byte) ( x & 0xFF );
		b[pos++] = (byte) ( ( x >> 8 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 16 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 24 ) & 0xFF );

		return 4;
	}

	public static int readInt( byte[] b, int pos ) {

		return ( ( b[pos + 3] & 0xFF ) << 24 )
				| ( ( b[pos + 2] & 0xFF ) << 16 )
				| ( ( b[pos + 1] & 0xFF ) << 8 )
				| ( b[pos] & 0xFF );

	}

	public static int writeBytes( long x, byte[] b, int pos ) {

		b[pos++] = (byte) ( x & 0xFF );
		b[pos++] = (byte) ( ( x >> 8 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 16 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 24 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 32 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 40 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 48 ) & 0xFF );
		b[pos++] = (byte) ( ( x >> 56 ) & 0xFF );

		return 8;
	}

	public static long readLong( byte[] b, int pos ) {

		return ( ( (long) b[pos + 7] & 0xFF ) << 56 )
				| ( ( (long) b[pos + 6] & 0xFF ) << 48 )
				| ( ( (long) b[pos + 5] & 0xFF ) << 40 )
				| ( ( (long) b[pos + 4] & 0xFF ) << 32 )
				| ( ( (long) b[pos + 3] & 0xFF ) << 24 )
				| ( ( (long) b[pos + 2] & 0xFF ) << 16 )
				| ( ( (long) b[pos + 1] & 0xFF ) << 8 )
				| ( (long) b[pos] & 0xFF );
	}
}
