package com.hoolai.ccgames.skeleton.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SynEnvRegistry {

    private ConcurrentMap< Long, SynEnvBase > mappings = new ConcurrentHashMap<>();

    public SynEnvBase get( long synId ) {
        return mappings.get( synId );
    }

    public SynEnvBase remove( long synId ) {
        return mappings.remove( synId );
    }

    public SynEnvBase put( long synId, SynEnvBase seb ) {
        return mappings.put( synId, seb );
    }
}
