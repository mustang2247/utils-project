package com.hoolai.ccgames.scandb.starter;

public class Starter {

	public static void main( String[] args ) {
		
		if( args.length == 0 ) {
    		System.err.println( "USAGE: java -jar ScanDB.jar nthread" );
    		return;
    	}
		
		int nthread = Integer.parseInt( args[0] );
		
		Scanner scanner = new FullScanner( "FullScanner" );
		scanner.run( nthread );
		
		System.exit( 0 );
	}
}
