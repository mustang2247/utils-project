package com.hoolai.ccgames.skeleton.base;

/**
 * Created by hoolai on 2016/7/19.
 */

@FunctionalInterface
public interface SynEnvExtractor {
    SynEnvBase extract( Object obj );
}
