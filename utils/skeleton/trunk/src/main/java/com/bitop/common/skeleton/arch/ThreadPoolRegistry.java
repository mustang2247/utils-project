/**
 * Author: guanxin
 * Date: 2015-08-06
 */

package com.bitop.common.skeleton.arch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ThreadPoolRegistry {

    private Map< String, ExecutorService > pools = new HashMap<>();

    private boolean active = false;

    public void shutdown() {
        if( active ) {
            pools.forEach( ( k, v ) -> v.shutdown() );
        }
    }

    public ExecutorService get( String poolName ) {
        return pools.get( poolName );
    }

    public ExecutorService set( String poolName, ExecutorService ses ) {
        active = true;
        return pools.put( poolName, ses );
    }
}
