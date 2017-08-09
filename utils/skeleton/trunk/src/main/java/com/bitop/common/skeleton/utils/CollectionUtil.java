package com.bitop.common.skeleton.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class CollectionUtil {

    public static < T > List< T > repeat( T x, int n ) {
        List< T > rv = new ArrayList<>( n );
        for( int i = 0; i < n; ++i ) rv.add( x );
        return rv;
    }

    public static < T > List< T > random( List< T > arr, int n ) {
        int[] tmp = new int[arr.size()];
        int sz = tmp.length;
        for( int i = 0; i < sz; ++i ) tmp[i] = i;
        n = Math.min( n, sz );
        List< T > rv = new ArrayList<>( n );
        for( int i = 0; i < n; ++i ) {
            int r = ThreadLocalRandom.current().nextInt( 0, sz );
            rv.add( arr.get( tmp[r] ) );
            tmp[r] = tmp[--sz];
        }
        return rv;
    }

    public static < T > boolean containsAll( Collection< T > c1, Collection< T > c2 ) {
        if( c1.size() < c2.size() ) return false;
        Set< T > tmp = new HashSet<>( c2 );
        for( T x : tmp ) {
            if( Collections.frequency( c1, x ) < Collections.frequency( c2, x ) ) return false;
        }
        return true;
    }

    public static < T1, T2 > boolean containsAll( Collection< T1 > c1, Collection< T2 > c2, BiPredicate< T1, T2 > pred ) {
        if( c1.size() < c2.size() ) return false;
        Set< T2 > tmp = new HashSet<>( c2 );
        for( T2 x2 : tmp ) {
            if( c1.stream().filter( x1 -> pred.test( x1, x2 ) ).count() < Collections.frequency( c2, x2 ) )
                return false;
        }
        return true;
    }

    public static < T > void subtract( Collection< T > c1, Collection< T > c2 ) {
        c2.forEach( c1::remove );
    }

    public static < T1, T2 > Map< T1, T2 > string2Map( String s, String splitter1, String splitter2,
                                                       Function< String, T1 > parser1, Function< String, T2 > parser2 ) {
        Map< T1, T2 > rv = new TreeMap<>();
        if( s.contains( splitter1 ) ) {
            String[] s1 = s.split( splitter1 );
            for( String s2 : s1 ) {
                if( s2.contains( splitter2 ) ) {
                    String[] s3 = s2.split( splitter2 );
                    rv.put( parser1.apply( s3[0] ), parser2.apply( s3[1] ) );
                }
            }
        }
        return rv;
    }

    public static < T > T random( Collection< T > src, Function< T, Double > weightFunc ) {
        if( src == null || src.size() == 0 ) return null;
        double totalWeight = src.stream().mapToDouble( x -> weightFunc.apply( x ) ).sum();
        double prob = ThreadLocalRandom.current().nextDouble( 0, totalWeight );
        for( T x : src ) {
            double w = weightFunc.apply( x );
            if( prob < w ) return x;
            prob -= w;
        }
        return src.iterator().next();
    }
}
