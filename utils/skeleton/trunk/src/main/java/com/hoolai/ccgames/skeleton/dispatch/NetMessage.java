package com.hoolai.ccgames.skeleton.dispatch;

public class NetMessage {

    private int kindId;

    private int msgId;

    private Object message;

    public NetMessage() {
    }

    public NetMessage( int kindId, Object message ) {
        super();
        this.kindId = kindId;
        this.message = message;
    }

    public NetMessage( int kindId, int msgId, Object message ) {
        super();
        this.kindId = kindId;
        this.msgId = msgId;
        this.message = message;
    }

    public int getKindId() {
        return kindId;
    }

    public void setKindId( int kindId ) {
        this.kindId = kindId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId( int msgId ) {
        this.msgId = msgId;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage( Object message ) {
        this.message = message;
    }

    public String toString() {
        return new StringBuilder( 128 ).append( "kindId:" ).append( kindId )
                .append( ",msgId:" ).append( msgId )
                .append( ",msg:" ).append( message.toString().replace( "\n", " " ) )
                .toString();
    }
}
