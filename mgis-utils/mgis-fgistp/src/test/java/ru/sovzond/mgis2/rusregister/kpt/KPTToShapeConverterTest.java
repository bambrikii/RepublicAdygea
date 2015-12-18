package ru.sovzond.mgis2.rusregister.kpt;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Alexander Arakelyan on 24.11.15.
 */
public class KPTToShapeConverterTest {

	@Test
	public void testProcess() throws IOException, SAXException, ParserConfigurationException {
		String absolutePath = new File(".").getAbsolutePath();
		String name = "doc22169826.xml";
		new KPTToShapeConverter(absolutePath).process(name, KPTToShapeConverterTest.class.getResourceAsStream(name));
	}
}
