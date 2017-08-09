package com.hoolai.ccgames.skeleton.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtil {

    public static final TimeZone defaultZone = TimeZone.getDefault();
    public static final long DAY_SECONDS = 24L * 60 * 60;
    public static final long DAY_MILLIS = 24L * 60 * 60 * 1000;
    public static final long HOUR_MILLIS = 60L * 60 * 1000;
    public static final long MINUTE_MILLIS = 60L * 1000;
    public static final long SECOND_MILLIS = 1000L;

    public static SimpleDateFormat ymdhmFormat() {
        return new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
    }

    public static SimpleDateFormat ymdhmsFormat() {
        return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    }

    public static SimpleDateFormat ymdFormat() {
        return new SimpleDateFormat( "yyyy-MM-dd" );
    }

    public static int getDays( long millis ) {
        return (int) ( ( millis + defaultZone.getOffset( millis ) ) / DAY_MILLIS );
    }

    public static int getSecondsToNextDay( long millis ) {
        long tmp = millis + TimeUtil.defaultZone.getOffset( millis );
        return (int) ( ( tmp / DAY_MILLIS * DAY_MILLIS + DAY_MILLIS - tmp ) / SECOND_MILLIS );
    }

    public static int getHours( long millis ) {
        return (int) ( ( millis + defaultZone.getOffset( millis ) ) / HOUR_MILLIS );
    }



    public static long getHourBeginMillis( long millis ) {
        long offset = TimeUtil.defaultZone.getOffset( millis );
        return ( millis + offset ) / HOUR_MILLIS * HOUR_MILLIS - offset;
    }

    public static long getDayBeginMillis( long millis ) {
        long offset = TimeUtil.defaultZone.getOffset( millis );
        return ( millis + offset ) / DAY_MILLIS * DAY_MILLIS - offset;
    }

    public static long getMillisToNextHour( long millis ) {
        return getHourBeginMillis( millis ) + HOUR_MILLIS - millis;
    }

    public static long getMillisToNextDay( long millis ) {
        return getDayBeginMillis( millis ) + DAY_MILLIS - millis;
    }

    public static boolean isSameDay( long m1, long m2 ) {
        return getDays( m1 ) == getDays( m2 );
    }

    public static List< Long > splitDays( long begGMT, long endGMT, TimeZone zone ) {

        if( zone == null ) zone = defaultZone;

        List< Long > rv = new LinkedList< Long >();

        Calendar ca = GregorianCalendar.getInstance( zone );
        ca.setTimeInMillis( begGMT );
        ca.add( Calendar.DATE, 1 );
        ca.set( Calendar.HOUR_OF_DAY, 0 );
        ca.set( Calendar.MINUTE, 0 );
        ca.set( Calendar.SECOND, 0 );
        ca.set( Calendar.MILLISECOND, 0 );

        rv.add( begGMT );
        long tmp = ca.getTimeInMillis();
        while( tmp < endGMT ) {
            rv.add( tmp );
            ca.add( Calendar.DATE, 1 );
            tmp = ca.getTimeInMillis();
        }
        rv.add( endGMT );
        return rv;
    }

    public static List< Long > splitWeeks( long begGMT, long endGMT, TimeZone zone ) {

        if( zone == null ) zone = defaultZone;

        List< Long > rv = new LinkedList< Long >();

        Calendar ca = GregorianCalendar.getInstance( zone );
        ca.setTimeInMillis( begGMT );
        ca.add( Calendar.WEEK_OF_MONTH, 1 );
        ca.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
        ca.set( Calendar.HOUR_OF_DAY, 0 );
        ca.set( Calendar.MINUTE, 0 );
        ca.set( Calendar.SECOND, 0 );
        ca.set( Calendar.MILLISECOND, 0 );

        rv.add( begGMT );
        long tmp = ca.getTimeInMillis();
        while( tmp < endGMT ) {
            rv.add( tmp );
            ca.add( Calendar.WEEK_OF_MONTH, 1 );
            tmp = ca.getTimeInMillis();
        }
        rv.add( endGMT );
        return rv;
    }

    public static List< Long > splitMonths( long begGMT, long endGMT, TimeZone zone ) {

        if( zone == null ) zone = defaultZone;

        List< Long > rv = new LinkedList< Long >();

        Calendar ca = GregorianCalendar.getInstance( zone );
        ca.setTimeInMillis( begGMT );
        ca.add( Calendar.MONTH, 1 );
        ca.set( Calendar.DAY_OF_MONTH, 1 );
        ca.set( Calendar.HOUR_OF_DAY, 0 );
        ca.set( Calendar.MINUTE, 0 );
        ca.set( Calendar.SECOND, 0 );
        ca.set( Calendar.MILLISECOND, 0 );

        rv.add( begGMT );
        long tmp = ca.getTimeInMillis();
        while( tmp < endGMT ) {
            rv.add( tmp );
            ca.add( Calendar.MONTH, 1 );
            tmp = ca.getTimeInMillis();
        }
        rv.add( endGMT );
        return rv;
    }

    public static List< Long > splitHours( long begGMT, long endGMT, TimeZone zone ) {

        if( zone == null ) zone = defaultZone;

        List< Long > rv = new LinkedList< Long >();

        Calendar ca = GregorianCalendar.getInstance( zone );
        ca.setTimeInMillis( begGMT );
        ca.add( Calendar.HOUR_OF_DAY, 1 );
        ca.set( Calendar.MINUTE, 0 );
        ca.set( Calendar.SECOND, 0 );
        ca.set( Calendar.MILLISECOND, 0 );

        rv.add( begGMT );
        long tmp = ca.getTimeInMillis();
        while( tmp < endGMT ) {
            rv.add( tmp );
            ca.add( Calendar.HOUR_OF_DAY, 1 );
            tmp = ca.getTimeInMillis();
        }
        rv.add( endGMT );
        return rv;
    }

    public static List< Long > splitTime( long begGMT, long endGMT, TimeZone zone, String type ) {
        switch( type ) {
            case "DAY":
                return splitDays( begGMT, endGMT, zone );
            case "WEEK":
                return splitWeeks( begGMT, endGMT, zone );
            case "MONTH":
                return splitMonths( begGMT, endGMT, zone );
            case "HOUR":
                return splitHours( begGMT, endGMT, zone );
            case "NONE":
                return Arrays.asList( begGMT, endGMT );
        }
        return null;
    }

    public static void main( String[] args ) {

        try {
            long gmt = System.currentTimeMillis();
            long offset = TimeUtil.defaultZone.getOffset( gmt );
            long local = gmt + offset;
            System.out.println( "offset = " + offset );
            System.out.println( "hourBegin = " + TimeUtil.getHourBeginMillis( gmt ) );
            System.out.println( "DayBegin = " + TimeUtil.getDayBeginMillis( gmt ) );
            System.out.println( "millisToNextHour = " + TimeUtil.getMillisToNextHour( gmt ) );
            System.out.println( "millisToNextDay = " + TimeUtil.getMillisToNextDay( gmt ) );
            System.out.println( "secondsToNextDay = " + TimeUtil.getSecondsToNextDay( gmt ) );
            System.out.println( "secondsToNextDay = " + TimeUtil.getMillisToNextDay( gmt ) / 1000 );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
