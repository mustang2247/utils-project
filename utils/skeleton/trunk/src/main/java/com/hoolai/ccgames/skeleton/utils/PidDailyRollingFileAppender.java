package com.hoolai.ccgames.skeleton.utils;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.helpers.QuietWriter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class PidDailyRollingFileAppender extends DailyRollingFileAppender {

    public PidDailyRollingFileAppender() {
        super();
        Runtime.getRuntime().addShutdownHook( new FlushThread() );
    }

    @Override
    public void setFile( String file ) {
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        String name = rt.getName();
        String pid = name.substring( 0, name.indexOf( "@" ) );
        super.setFile( file + "-" + pid + ".log" );
    }

    public QuietWriter getQw() {
        return super.qw;
    }

    private class FlushThread extends Thread {
        @Override
        public void run() {
            QuietWriter qw = PidDailyRollingFileAppender.this.getQw();
            if( qw != null ) {
                qw.flush();
            }
        }
    }
}
