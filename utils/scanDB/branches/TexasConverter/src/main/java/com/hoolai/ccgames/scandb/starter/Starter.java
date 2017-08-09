package com.hoolai.ccgames.scandb.starter;

public class Starter {

    public static void main( String[] args ) {

        if( args.length == 0 ) {
            System.err.println( "USAGE: java -jar ScanDB.jar -scanner scannerName -thread n [-test userId -beg userId -end userId]" );
            return;
        }

        String scannerName = "";
        int nthread = 1;
        long testUserId = -1;
        long userIdBeg = -1;
        long userIdEnd = -1;

        for( int i = 0; i < args.length; ++i ) {
            if( "-scanner".equals( args[i] ) ) {
                scannerName = args[++i];
            }
            else if( "-thread".equals( args[i] ) ) {
                nthread = Integer.parseInt( args[++i] );
            }
            else if( "-test".equals( args[i] ) ) {
                testUserId = Long.parseLong( args[++i] );
            }
            else if( "-beg".equals( args[i] ) ) {
                userIdBeg = Long.parseLong( args[++i] );
            }
            else if( "-end".equals( args[i] ) ) {
                userIdEnd = Long.parseLong( args[++i] );
            }
        }

        Scanner scanner = null;
        switch( scannerName ) {
            case "Texaspoker": {
                scanner = new TexasScanner( "TexasScanner", userIdBeg, userIdEnd );
                break;
            }
            case "Test": {
                scanner = new TestScanner( "TestScanner", testUserId );
                break;
            }
            case "CenterFixer": {
                scanner = new CenterFixer( "CenterFixer" );
                break;
            }
            case "ItemLister": {
                scanner = new ItemLister( "ItemLister", testUserId );
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
