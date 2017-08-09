package com.hoolai.ccgames.dumpDB.test;

import java.io.IOException;

import com.hoolai.ccgames.center.datatypes.ItemList;
import com.hoolai.ccgames.center.datatypes.ItemUnit;
import com.hoolai.ccgames.center.repo.GlobalRepo;
import com.hoolai.ccgames.center.repo.UserRepo;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.internal.OperationFuture;


public class DBTest {
	
	public void testGet() {
		
//		MemcachedClient  mc = null;
//		
//		try {
//			mc = new MemcachedClient(
//					new ConnectionFactoryBuilder().setProtocol(Protocol.TEXT).build(),
//					AddrUtil.getAddresses("192.168.1.223:11211"));
//			
//			long uid = (Long)mc.get( "MIXI:100632434:qweqweqweqwe" );
//			System.out.println( "UID: " + uid );
//			
//			UserRepo.getInstance().setMC( mc );
//			GlobalRepo.getInstance().setMC( mc );
//			
//			System.out.println( "Gold: " + UserRepo.getInstance().getGold( uid ) );
//			System.out.println( "HGold: " + UserRepo.getInstance().getHoolaiGold( uid ) );
//			System.out.println( "MP: " + UserRepo.getInstance().getMasterPoint( uid ) );
//			System.out.println( "HelpUsed: " + UserRepo.getInstance().getHelpUsed( uid ) );
//			
//			
//			System.out.print( "Items: [" );
//			ItemList list = UserRepo.getInstance().getItems( uid );
//			if( list != null )
//				for(  ItemUnit item : list.items ) System.out.printf( "%d,%d\t", item.itemID, item.itemCount ); 
//			System.out.print( "]\n" );
//			
//			System.out.print( "ItemUsed: [" );
//			list = UserRepo.getInstance().getItemUsed( uid );
//			if( list != null )
//				for(  ItemUnit item : list.items ) System.out.printf( "%d,%d\t", item.itemID, item.itemCount ); 
//			System.out.print( "]\n" );
//			
//		}
//		catch( IOException e ) {
//			e.printStackTrace();
//		}
//		finally {
//			if( mc != null ) mc.shutdown();
//		}
	}
	
	public static void main(String[] args) {
		new DBTest().testGet();
	}
}
