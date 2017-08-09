package com.hoolai.ccgames.skeleton.net;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnRegistry {

    private ConcurrentMap< Long, Channel > uid2channel = new ConcurrentHashMap< Long, Channel >();

    public ConnRegistry() {
    }

    public Channel put( long userId, Channel ctx ) {
        return uid2channel.put( userId, ctx );
    }

    public Channel get( long userId ) {
        return uid2channel.get( userId );
    }

    public void remove( long userId ) {
        uid2channel.remove( userId );
    }

    public boolean remove( long userId, Channel channel ) {
        return uid2channel.remove( userId, channel );
    }

    public Collection< Channel > all() {
        return uid2channel.values();
    }
}