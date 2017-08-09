package com.hoolai.ccgames.skeleton.base;

import com.hoolai.ccgames.skeleton.arch.EventLoop;

import java.util.concurrent.TimeUnit;

public class ActiveTableGroupBase {

    private EventLoop eventLoop;

    public ActiveTableGroupBase( long interval, TimeUnit tu ) {
        eventLoop = new EventLoop();
    }

    public void addTable( TableBase table ) {
        eventLoop.register( table );
    }

    public void removeTable( TableBase table ) {
        eventLoop.unregister( table );
    }

}
