package com.hoolai.ccgames.scandb.starter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Starter {

    public static void main( String[] args ) {

        if( args.length == 0 ) {
            System.err.println( "USAGE: java -jar ScanDB.jar -scanner scannerName -range-id rangeId1,rangeId2,... -thread n [-scan-old]" );
            return;
        }

        String scannerName = "";
        int nthread = 1;
        boolean scanOld = false;
        List< Integer > rangeIds = null;

        for( int i = 0; i < args.length; ++i ) {
            if( "-scanner".equals( args[i] ) ) {
                scannerName = args[++i];
            }
            else if( "-thread".equals( args[i] ) ) {
                nthread = Integer.parseInt( args[++i] );
            }
            else if( "-range-id".equals( args[i] ) ) {
                rangeIds = Arrays.asList( args[++i].split( "," ) ).stream()
                        .map( Integer::parseInt )
                        .collect( Collectors.toList() );
            }
            else if( "-scan-old".equals( args[i] ) ) {
                scanOld = true;
            }
        }

        Scanner scanner = null;
        switch( scannerName ) {
            case "Texaspoker": {
                scanner = new TexasScanner( "TexasScanner", rangeIds );
                break;
            }
            case "Mahjong": {
                scanner = new MahjongScanner( "MahjongScanner", rangeIds, scanOld );
                break;
            }
            case "Landlords": {
                scanner = new LandlordsScanner( "LandlordsScanner", rangeIds );
                break;
            }
            case "Fish": {
                scanner = new FishScanner( "FishScanner", rangeIds );
                break;
            }
            default: {
                throw new RuntimeException( "Invalid scanner name!" );
            }
        }
        scanner.run( nthread );

        System.exit( 0 );
    }
}
