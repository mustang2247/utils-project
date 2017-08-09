package com.bitop.common.skeleton.dispatch;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CommandLoader extends DefaultHandler {

    private Integer key = null;
    private CommandRegistry registry = null;
    private CommandProperties command = null;
    private String tagName = null;
    private StringBuilder data = null;

    public CommandLoader( CommandRegistry registry ) {
        this.registry = registry;
    }

    public void startElement( String uri, String localName, String qName,
                              Attributes attributes ) throws SAXException {
        if( qName.equals( "command" ) ) {
            command = new CommandProperties();
        }
        this.tagName = qName;
        data = new StringBuilder();
    }

    @Override
    public void endElement( String uri, String localName, String qName )
            throws SAXException {
        if( qName.equals( "command" ) ) {
            if( this.registry.put( key, command ) != null ) {
                throw new RuntimeException( "duplicate for command " + key );
            }
        }
        this.tagName = null;
    }

    @Override
    public void characters( char[] ch, int start, int length )
            throws SAXException {
        if( this.tagName != null ) {
            data.append( ch, start, length );
            switch( this.tagName ) {
                case "kindID":
                    this.key = Integer.parseInt( data.toString().trim() );
                    break;
                case "remoteObject":
                    this.command.remoteObjectClassName = data.toString().trim();
                    break;
                case "service":
                    this.command.serviceClassName = data.toString().trim();
                    break;
                case "method":
                    this.command.serviceMethodName = data.toString().trim();
                    break;
                case "domain":
                    this.command.domain = data.toString().trim();
                    break;
            }
            data.setLength( 0 );
        }
    }
}
