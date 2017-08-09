package com.bitop.common.skeleton.base;

import com.bitop.common.skeleton.arch.EventLoop;

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
