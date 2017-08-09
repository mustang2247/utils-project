package com.hoolai.ccgames.skeleton.arch;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.hoolai.ccgames.skeleton.base.Finalizer;
import com.hoolai.ccgames.skeleton.base.Initializer;
import com.hoolai.ccgames.skeleton.config.ClientConfig;

public class GameClient {
	
	// 通过linux信号退出
	private static class SafeExitHandler implements SignalHandler {

		private static final Logger logger = LoggerFactory.getLogger( SafeExitHandler.class );
		
		private GameClient gameClient;
		
		public SafeExitHandler( GameClient gc ) {
			gameClient = gc;
		}
		
		@Override
		public void handle( Signal arg0 ) {
			logger.info( "GameClient-{} receive TERM signal, will stop!", gameClient.getName() );
			gameClient.stopClient();
		}
	}
	
	// 这是在jvm退出时会调用的hook
	// 如果不是正常途径退出，希望给服务器一个清理的机会
	// 但是如果发送kill -9，那就没有机会了
	private static class CleanupThread extends Thread {

		private static final Logger logger = LoggerFactory.getLogger( CleanupThread.class );
		
		private GameClient gameClient;
		
		public CleanupThread( GameClient gs ) {
			gameClient = gs;
		}
		
		@Override
		public void run() {
			if( gameClient.getClientState() != ClientState.DESTROY ) {
				logger.info( "GameClient-{} abnormal exit, enter CleanupThread, will stop!", gameClient.getName() );
				gameClient.stopClient();
			}
		}
	}
	
	public enum ClientState {
		BORN, INIT, RUN, SUSPEND, DESTROY
	}
	
	private static final Logger logger = LoggerFactory.getLogger( GameClient.class );
	
	protected String name;
	
	protected ClientConfig config;
	
	protected volatile ClientState clientState = ClientState.BORN;  // 服务状态
	
	protected Bootstrap bootstrap = null;

	protected EventLoopGroup eventLoopGroup = null;

	protected ChannelFuture channelFuture = null;
	
	protected ChannelInitializer< SocketChannel > channelInitializer = null;
	
	protected Initializer initializer = null;
	
	protected Finalizer finalizer = null;
	
	protected boolean active = false;
	
	public GameClient( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setClientState( ClientState state ) {
		this.clientState = state;
	}

	public ClientState getClientState() {
		return clientState;
	}

	public void pauseClient() {
		setClientState( ClientState.SUSPEND );
	}

	public void resumeClient() {
		setClientState( ClientState.RUN );
	}
	
	public GameClient setChannelInitializer( ChannelInitializer< SocketChannel > ci ) {
		channelInitializer = ci;
		return this;
	}
	
	public GameClient setInitializer( Initializer init ) {
		initializer = init;
		return this;
	}
	
	public GameClient setFinalizer( Finalizer f ) {
		finalizer = f;
		return this;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive( boolean b ) {
		active = b;
	}
	
	public void initClient( ClientConfig cfg ) {
		
		this.config = cfg;
		
		if( initializer != null ) initializer.init();
		
		setClientState( ClientState.INIT );
	}
	
	public void initClient( String configFilePath ) {
		
		ClientConfig cfg = new ClientConfig();
		cfg.init( configFilePath );
		
		initClient( cfg );
	}
	
	public Channel getChannel() {
		return channelFuture.channel();
	}
	
	public void startClient() {
		startClient( 1 );
	}
	
	public void startClient( int nthreads ) {
		startClient( nthreads, true );
	}
	
	public void startClient( int nthreads, boolean installSignal ) {
		
		if( this.channelInitializer == null ) {
			logger.error( "GameClient-{} channelInitializer is NULL, start fail!", name );
			throw new RuntimeException();
		}
		
		if( this.getClientState() != ClientState.INIT && this.getClientState() != ClientState.RUN ) {
			logger.error( "GameClient-{} is not init, start fail!", name );
			throw new RuntimeException();
		}
		
		if( installSignal ) {
			// Runtime.getRuntime().addShutdownHook( new CleanupThread( this ) );
			Signal.handle( new Signal( "TERM" ), new SafeExitHandler( this ) );
		}
		
		eventLoopGroup = new NioEventLoopGroup( nthreads );
		bootstrap = new Bootstrap();
		bootstrap.group( eventLoopGroup )
				.channel( NioSocketChannel.class )
				.option( ChannelOption.TCP_NODELAY, true )
				.option( ChannelOption.SO_KEEPALIVE, true )
				.handler( channelInitializer );
		
		channelFuture = bootstrap
				.connect( config.SERVER_ADDR, config.SERVER_PORT )
				.addListener( new ConnectionListener( this ) );
		
		setClientState( ClientState.RUN );
		logger.info( "GameClient-{} start, connect to {}:{}",
				name, config.SERVER_ADDR, config.SERVER_PORT );
	}
	
	public void reconnect() {
		try {
			if(getClientState() == ClientState.DESTROY){
				return;
			}
			channelFuture = bootstrap
					.connect( config.SERVER_ADDR, config.SERVER_PORT )
					.addListener( new ConnectionListener( this ) );
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}
	
	public void stopClient() {
		
		if( getClientState() == ClientState.DESTROY )
			return;
		
		setClientState( ClientState.DESTROY );
		
		logger.info( "GameClient-{} stopping...", name );
		
		try {
			logger.info( "GameClient-{} stopping2... finalizer is {}", name, ( finalizer == null ? "null" : "not null" ) );
			if( finalizer != null ) finalizer.end();
			eventLoopGroup.shutdownGracefully().sync();
			
			logger.info( "GameClient-{} stopped!", name );
		}
		catch( Exception e ) {
			logger.error( e.getMessage(), e );
		}
	}
}
