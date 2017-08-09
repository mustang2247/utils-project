package com.hoolai.ccgames.pressureTest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hoolai.ccgames.config.ServerConfig;
import com.hoolai.ccgames.netty.ProtobufClientHandler;
import com.hoolai.ccgames.netty.ProtobufClientInitializer;
import com.hoolai.ccgames.utils.CommandWrapper;
import com.hoolai.ccgames.protocol.Auth;
import com.hoolai.ccgames.protocol.Auth.LoginInfo;
import com.hoolai.ccgames.protocol.Command.BaseCommand;
import com.hoolai.ccgames.protocol.Command.BaseCommand.CommandType;
import com.hoolai.ccgames.protocol.UserModify.ItemGiveRecord;
import com.hoolai.ccgames.protocol.UserModify.ItemUnit;
import com.hoolai.ccgames.protocol.UserModify.UserID;
import com.hoolai.ccgames.protocol.UserModify.UserInfo;
import com.hoolai.ccgames.protocol.UserModify.UserItemGiveList;
import com.hoolai.ccgames.protocol.UserModify.UserItemList;
import com.hoolai.ccgames.protocol.UserModify.UserLongValue;

public class Main {
	
	public final static Logger logger = LoggerFactory
			.getLogger( Main.class );
	
	public static AtomicInteger counter = new AtomicInteger(20);
	
	public static int MAX_LOOP;
	public static int MAX_THREAD;
	public static int SLEEP_MILLIS;
	public static BaseCommand logincmd;
	
	public Random  rand = new Random();
	

	
	public void run() {
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group( group )
					.channel( NioSocketChannel.class )
					.handler( new ProtobufClientInitializer() );

			ChannelFuture f = b.connect( ServerConfig.gateIP, ServerConfig.gatePort ).sync();
			Channel ch = f.channel();
			
			// login first
			ch.pipeline().writeAndFlush( logincmd );
			
			List< Long >  snd = new LinkedList< Long >();
			
			int RAND_SPACE = 41;
			for( int i = 0; i < MAX_LOOP; ++i ) {
				int x = rand.nextInt( RAND_SPACE );
				int z = RAND_SPACE;
				if( x == --z ) runGetHoolaiGold( ch );
				else if( x == --z ) runChangeHoolaiGold( ch );
				else if( x == --z ) runGetHelpUsed( ch );
				else if( x == --z ) runIncrHelpUsed( ch );
				else if( x == --z ) runGetMasterPoint( ch );
				else if( x == --z ) runChangeMasterPoint( ch );
				
				else if( x == --z ) runChangeItem( ch );
				else if( x == --z ) runGetItem( ch );
				else if( x == --z ) runGetItemUsed( ch );
				else if( x == --z ) runAddItemUsed( ch );
				else if( x == --z ) runAddItemGive( ch );
				else if( x == --z ) runGetItemGive( ch );
				
				else if( x > 20 ) runChangeGold( ch );
				else runGetGold( ch );

    			snd.add( System.currentTimeMillis() );
    			
    			Thread.sleep( SLEEP_MILLIS );
			}
			
			logger.info( "SEND FINISH" );
			Thread.sleep( 5000 );
			
			ProtobufClientHandler pch = (ProtobufClientHandler) (ch.pipeline().get( "MessageHandler" ));
			
			logger.info( "{}", snd.size() );
			logger.info( "{}", pch.rcv.size() );
			
			for( int i = MAX_LOOP - 20; i < MAX_LOOP; ++i ) {
				logger.info( "{},{},{}", 
						snd.get( i ),
						pch.rcv.get( i ),
						pch.rcv.get( i ) - snd.get( i ) );
			}
			
			ch.pipeline().flush();
			ch.closeFuture().sync();

		}
		catch( InterruptedException e ) {
			logger.error( e.getMessage(), e );
		}
		finally {
			try {
				group.shutdownGracefully().sync();
			}
			catch( InterruptedException e ) {
				logger.error( e.getMessage(), e );
			}
		}
		
	}
	
	public int randUid() {
		return rand.nextInt( 10000 ) + 10001;
	}
	
	public int randItemid() {
		return rand.nextInt( 100 ) + 1;
	}
	
	public void runGetUserID( Channel ch ) {
		UserInfo uinfo = UserInfo.newBuilder().setPlatform( "MIXI" )
				.setAppId( "12345" ).setOpenId( "llftest1" ).build();
		BaseCommand cmd = CommandWrapper
				.wrap( 0, CommandType.GET_USER_ID, UserInfo.cmd,
						uinfo );
		ch.pipeline().writeAndFlush( cmd );
	}
	
	public void runGetGold( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getGoldCmd = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_GOLD, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getGoldCmd );
	}
	
	public void runChangeGold( Channel ch ) {
		UserLongValue gChange = UserLongValue.newBuilder().
				setUserId( randUid() ).setLongValue( 10 ).build();
		BaseCommand gChangeCmd = CommandWrapper
				.wrap( 0, CommandType.CHANGE_USER_GOLD, UserLongValue.cmd,
						gChange );
		ch.pipeline().writeAndFlush( gChangeCmd );
	}
	
	public void runGetHoolaiGold( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getHGold = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_HOOLAI_GOLD, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getHGold );
	}
	
	public void runChangeHoolaiGold( Channel ch ) {
		UserLongValue gChange = UserLongValue.newBuilder().
				setUserId( randUid() ).setLongValue( 10 ).build();
		BaseCommand gChangeCmd = CommandWrapper
				.wrap( 0, CommandType.CHANGE_USER_HOOLAI_GOLD, UserLongValue.cmd,
						gChange );
		ch.pipeline().writeAndFlush( gChangeCmd );
	}
	
	public void runGetMasterPoint( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getMP = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_MASTER_POINT, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getMP );
	}
		
	public void runChangeMasterPoint( Channel ch ) {
		UserLongValue gChange = UserLongValue.newBuilder().
				setUserId( randUid() ).setLongValue( 10 ).build();
		BaseCommand gChangeCmd = CommandWrapper
				.wrap( 0, CommandType.CHANGE_USER_MASTER_POINT, UserLongValue.cmd,
						gChange );
		ch.pipeline().writeAndFlush( gChangeCmd );
	}
	
	public void runGetHelpUsed( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getHU = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_HELP_USED, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getHU );
	}
	
	public void runIncrHelpUsed( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand addHU = CommandWrapper
				.wrap( 0, CommandType.INCR_USER_HELP_USED, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( addHU );
	}
	
	public void runGetItem( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getItemcmd = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_ITEM, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getItemcmd );
	}
	
	public void runChangeItem( Channel ch ) {
		ItemUnit item2 = ItemUnit.newBuilder()
				.setItemId( randItemid() ).setItemCount( 20 ).setExpireTime( 0L )
				.build();
		UserItemList cg = UserItemList.newBuilder().setUserId( randUid() )
				.addItems( item2 ).build();
		BaseCommand changeItemcmd = CommandWrapper
				.wrap( 0, CommandType.CHANGE_USER_ITEM, UserItemList.cmd,
						cg );
		ch.pipeline().writeAndFlush( changeItemcmd );
	}
	
	public void runGetItemUsed( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getItemUsed = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_ITEM_USED, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getItemUsed );
	}
	
	public void runAddItemUsed( Channel ch ) {
		ItemUnit item2 = ItemUnit.newBuilder().
				setItemId( randItemid() ).setItemCount( 1 ).setExpireTime( 0L )
				.build();
		UserItemList cg = UserItemList.newBuilder().setUserId( randUid() )
				.addItems( item2 ).build();
		BaseCommand used = CommandWrapper
				.wrap( 0, CommandType.ADD_USER_ITEM_USED, UserItemList.cmd,
						cg );
		ch.pipeline().writeAndFlush( used );
	}
	
	public void runGetItemGive( Channel ch ) {
		UserID userID = UserID.newBuilder().setUserId( randUid() ).build();
		BaseCommand getItemGive = CommandWrapper
				.wrap( 0, CommandType.QUERY_USER_ITEM_GIVE, UserID.cmd,
						userID );
		ch.pipeline().writeAndFlush( getItemGive );
	}
	
	public void runAddItemGive( Channel ch ) {
		ItemGiveRecord r = ItemGiveRecord.newBuilder()
				.setSenderId( randUid() )
				.setReceiverId( randUid() )
				.setTime( System.currentTimeMillis() )
				.setItemId( randItemid() )
				.setItemCount( 1 )
				.setStatus( 0 ).build();
		UserItemGiveList cg = UserItemGiveList.newBuilder().setUserId( r.getSenderId() )
				.addRecords( r ).build();
		BaseCommand give = CommandWrapper
				.wrap( 0, CommandType.ADD_USER_ITEM_GIVE, UserItemGiveList.cmd,
						cg );
		ch.pipeline().writeAndFlush( give );
	}
	
	
	public void run2() {
		
	}
	
	public static void init() {
		
		ServerConfig.init();
		
		LoginInfo loginInfo = LoginInfo.newBuilder().
				setName( ServerConfig.loginName ).setPasswd( ServerConfig.loginPass )
				.setVersion( ServerConfig.messageVersion ).build();
		logincmd = BaseCommand.newBuilder()
				.setType( BaseCommand.CommandType.AUTH_LOGIN )
				.setExtension( Auth.LoginInfo.cmd, loginInfo )
				.build();
	}

	public static void main( String[] args ) throws Exception {

		if( args.length != 3 ) {
			System.err.printf( "Usage: pressureTest nthread sendInterval loopCount\n" );
			return;
		}
		
		init();
		
		MAX_THREAD = Integer.parseInt( args[0] );
		SLEEP_MILLIS = Integer.parseInt( args[1] );
		MAX_LOOP = Integer.parseInt( args[2] );
		
		for( int i = 0; i < MAX_THREAD; ++i ) {
			new Thread( new Runnable() {

				@Override
				public void run() {
					new Main().run();
				}
			} ).start();
		}

	}
}
