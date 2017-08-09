package com.hoolai.ccgames.scandb.starter;

public class Starter {

    public static void main( String[] args ) {

        if( args.length == 0 ) {
            System.err.println( "USAGE: java -jar ScanDB.jar -scanner scannerName -thread n [-scan-old]" );
            return;
        }

        String scannerName = "";
        int nthread = 1;
        boolean scanOld = false;

        for( int i = 0; i < args.length; ++i ) {
            if( "-scanner".equals( args[i] ) ) {
                scannerName = args[++i];
            }
            else if( "-thread".equals( args[i] ) ) {
                nthread = Integer.parseInt( args[++i] );
            }
            else if( "-scan-old".equals( args[i] ) ) {
                scanOld = true;
            }
        }

        Scanner scanner = null;
        switch( scannerName ) {
            case "Texaspoker": {
                scanner = new TexasScanner( "TexasScanner" );
                break;
            }
            case "Mahjong": {
                scanner = new MahjongScanner( "MahjongScanner", scanOld );
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
