package com.hoolai.ccgames.excel2xml.starter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Starter {

    public static Map< String, String > getMapping( String s ) {
        Map< String, String > rv = new HashMap< String, String >();
        String[] arr = s.split( ";" );
        for( String tmp : arr ) {
            if( tmp.contains( ":" ) ) {
                String[] arr2 = tmp.split( ":" );
                rv.put( arr2[0].trim(), arr2[1].trim() );
            }
        }
        return rv;
    }

    public static String getCellValue( XSSFCell cell ) {
        if( cell == null ) return "";

        String rv = cell.getCellType() == Cell.CELL_TYPE_STRING ?
                cell.getStringCellValue() : cell.getRawValue();
        return rv == null ? "" : rv;
    }

    public static void writeXML( Document doc, String fileName, String encoding ) throws Exception {
        FileOutputStream fos = new FileOutputStream( fileName );
        OutputStreamWriter osw = new OutputStreamWriter( fos, "UTF-8" );

        Source source = new DOMSource( doc );
        Result res = new StreamResult( osw );
        Transformer xformer = TransformerFactory.newInstance()
                .newTransformer();
        xformer.setOutputProperty( OutputKeys.ENCODING, encoding );
        xformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        xformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "4" );
        xformer.transform( source, res );

        osw.close();
        fos.close();
    }

    public static void main( String[] args ) throws Exception {

        if( args.length < 6 ) {
            System.err.println( "Usage: java -jar Excel2Xml.jar xlsxFile sheetIndex xmlFile rootName elementName keyMappings\n"
                    + "\te.g. java -jar Excel2Xml.jar abc.xlsx 0 back.xml fishes fish '类型ID:typeId;名称:name'\n"
                    + "\twill generate output like:\n"
                    + "\t<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "\t<fishes>\n"
                    + "\t\t<fish>\n"
                    + "\t\t\t<typeId>1</typeId>\n"
                    + "\t\t\t<name>小鱼儿</name>\n"
                    + "\t\t</fish>\n"
                    + "\t</fishes>" );
            System.exit( -1 );
        }

        System.out.println( "start parse..." );

        String inputFile = args[0].trim();
        int sheetIndex = Integer.parseInt( args[1].trim() );
        String outputFile = args[2].trim();
        String rootName = args[3].trim();
        String elementName = args[4].trim();
        Map< String, String > name2name = getMapping( args[5] );

        InputStream is = new FileInputStream( new File( inputFile ) );
        XSSFWorkbook wb = new XSSFWorkbook( is );
        XSSFSheet st = wb.getSheetAt( sheetIndex );
        XSSFRow row = null;
        XSSFCell cell = null;

        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        Element root = doc.createElement( rootName );
        doc.appendChild( root );

        row = st.getRow( 0 );
        List< String > head = new ArrayList<>();
        for( short colIndex = row.getFirstCellNum(); colIndex < row.getLastCellNum(); ++colIndex ) {
            cell = row.getCell( colIndex );
            head.add( getCellValue( cell ) );
        }

        for( int rowIndex = 1; rowIndex <= st.getLastRowNum(); ++rowIndex ) {
            row = st.getRow( rowIndex );
            Element ele = doc.createElement( elementName );
            root.appendChild( ele );
            for( short colIndex = 0; colIndex < row.getLastCellNum() && colIndex < head.size(); ++colIndex ) {

                String attrName = name2name.get( head.get( colIndex ) );
                if( attrName == null ) continue;

                cell = row.getCell( colIndex );
                Element attr = doc.createElement( attrName );
                Text text = doc.createTextNode( getCellValue( cell ) );

                attr.appendChild( text );
                ele.appendChild( attr );
            }
        }

        wb.close();
        is.close();

        writeXML( doc, outputFile, "UTF-8" );

        System.exit( 0 );
    }

}
