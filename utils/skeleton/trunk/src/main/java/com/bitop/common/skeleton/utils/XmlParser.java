package com.bitop.common.skeleton.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by hoolai on 2016/9/1.
 */
public class XmlParser extends DefaultHandler {

    private String elementName = null;
    private boolean convertSpecial = false;
    private StringBuilder data = new StringBuilder();

    public XmlParser( String elementName, boolean convertSpecial ) {
        this.elementName = elementName;
        this.convertSpecial = convertSpecial;
    }

    protected void init() {}
    protected void finish() {}
    protected void begin() {}
    protected void end() {}

    protected void process( String tag, String data ) {
    }

    protected String convert( String data ) {
        return data.replace( "&lt;", "<" )
                .replace( "&gt;", ">" )
                .replace( "&amp;", "&" )
                .replace( "&apos;", "'" )
                .replace( "&quot;", "\"" );
    }

    @Override
    public void startDocument() {
        init();
    }

    @Override
    public void endDocument() {
        finish();
    }

    @Override
    public void startElement( String uri, String localName, String qName,
                              Attributes attributes ) throws SAXException {
        if( qName.equals( elementName ) ) begin();
        data.setLength( 0 );
    }

    @Override
    public void endElement( String uri, String localName, String qName )
            throws SAXException {
        if( qName.equals( elementName ) ) end();
        else if( qName != null ) {
            process( qName, convertSpecial ? convert( data.toString() ) : data.toString() );
            data.setLength( 0 );
        }
    }

    @Override
    public void characters( char[] ch, int start, int length )
            throws SAXException {
        data.append( ch, start, length );
    }
}
