package com.hoolai.ccgames.skeleton.utils;

import org.apache.log4j.DailyRollingFileAppender;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class PidDailyRollingFileAppender extends DailyRollingFileAppender {

    @Override
    public void setFile( String file ) {
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        String name = rt.getName();
        String pid = name.substring( 0, name.indexOf( "@" ) );
        super.setFile( file + "-" + pid + ".log" );
    }
}
