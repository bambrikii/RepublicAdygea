package ru.sovzond.mgis2.fgistp;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.sax_handlers.EntryHandler;
import ru.sovzond.mgis2.fgistp.sax_handlers.FeedHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class ContentParser {
	Entry parseEntry(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		EntryHandler handler = new EntryHandler();
		saxParser.parse(inputStream, handler);
		return handler.getParsedContent();
	}

	public Entry parseChildren(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		FeedHandler handler = new FeedHandler();
		saxParser.parse(inputStream, handler);
		return handler.getContent();
	}

	public Entry parseDocuments(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		FeedHandler handler = new FeedHandler();
		saxParser.parse(inputStream, handler);
		return handler.getContent();
	}

	public Entry parseFiles(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		FeedHandler handler = new FeedHandler();
		saxParser.parse(inputStream, handler);
		return handler.getContent();
	}
}
