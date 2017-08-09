package com.bitop.common.skeleton.base;

/**
 * Created by hoolai on 2016/7/19.
 */

@FunctionalInterface
public interface SynEnvExtractor {
    SynEnvBase extract( Object obj );
}
