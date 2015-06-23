package com.dotit.gireve.com;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author H.L - admin
 *
 */
public class SoapErrorHandler extends DefaultHandler {

	protected final String TAG = getClass () .getSimpleName();
	
	private final String faultstring 		= "faultstring";
	
	private String errorString;

	// Buffer for a tag XML data
	protected StringBuffer buffer;


	public SoapErrorHandler () {
		super ();
	}

	// receives byte from the parser
	public void characters(char[] ch,int start, int length)	throws SAXException{

		String lecture = new String(ch,start,length);
		if(buffer != null) 
			buffer.append(lecture);
	}



	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		buffer = new StringBuffer();
		
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if (localName.equalsIgnoreCase(faultstring)){
			errorString = buffer.toString();
		}
	}
	
	
	public String getResult(){
		return errorString;
	}

}
