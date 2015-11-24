package ru.sovzond.mgis2.rusregister.kpt;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.rusregister.kpt.sax_handler.KPTHandler;
import ru.sovzond.mgis2.rusregister.kpt.shapefile_handlers.KPTShapefileWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class KPTToShapeConverter {

	private String dir;

	public KPTToShapeConverter(String dir) {
		this.dir = dir;
	}

	public void process(String name, InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		KPTShapefileWriter writer = new KPTShapefileWriter(dir, name);
		KPTHandler handler = new KPTHandler(writer);
		saxParser.parse(inputStream, handler);
	}
}
