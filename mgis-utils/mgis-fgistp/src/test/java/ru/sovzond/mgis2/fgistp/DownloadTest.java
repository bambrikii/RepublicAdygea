package ru.sovzond.mgis2.fgistp;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.ContentReader;
import ru.sovzond.mgis2.fgistp.fs_handlers.EntityPersistHandler;
import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.http_handlers.Downloadable;
import ru.sovzond.mgis2.fgistp.http_handlers.EntityDownloadHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class DownloadTest {
	private static final String DOWNLOAD_DIR = "/home/asd/fgistp3/";

	@Test
	public void testDownload() throws IOException, ParserConfigurationException, SAXException {
		Persistable persistHandler = new EntityPersistHandler(DOWNLOAD_DIR);
		Downloadable downloadHandler = new EntityDownloadHandler();
		DownloadApp app = new DownloadApp(downloadHandler, persistHandler);
		app.downloadEntry("(23366M)");
	}

	@Test
	public void testDownloadRecursive() throws IOException, ParserConfigurationException, SAXException, JAXBException {
		Persistable persistHandler = new EntityPersistHandler(DOWNLOAD_DIR);
		Downloadable downloadHandler = new EntityDownloadHandler();
		DownloadApp app = new DownloadApp(downloadHandler, persistHandler);
		app.downloadRecursively(new ArrayList<>(), "http://fgis.economy.gov.ru/Applications/FGIS_PROM/Strategis.Server.FGIS.DataService/FGISDataService.svc/KTDs(23366M)");
		app.await();
	}


	@Test
	public void testParse() throws IOException, ParserConfigurationException, SAXException {
		ContentParser parser = new ContentParser();
		Entry ktd = parser.parseEntry(DownloadTest.class.getResourceAsStream("Entry.xml"));
		System.out.println(ktd);
	}

	@Test
	public void testChildren() throws IOException, ParserConfigurationException, SAXException {
		ContentParser parser = new ContentParser();
		Entry childrenFeed = parser.parseChildren(DownloadTest.class.getResourceAsStream("Children.xml"));
		printEntry(childrenFeed);
	}

	private void printEntry(Entry childrenFeed) {
		System.out.println(String.format("ID: %s .", childrenFeed.getId()));
		for (Entry parent : childrenFeed.getEntries()) {
			System.out.println(String.format("EntryID: %s .", parent.getId()));
			for (Map.Entry<String, String> entry : parent.getProperties().entrySet()) {
				System.out.println(String.format("\tPROPERTY: %s : %s", entry.getKey(), entry.getValue()));
			}
		}
	}

	@Test
	public void testDocuments() throws IOException, ParserConfigurationException, SAXException {
		ContentParser parser = new ContentParser();
		Entry documents = parser.parseDocuments(DownloadTest.class.getResourceAsStream("DOCUMENTS.xml"));
		printEntry(documents);
	}

	@Test
	public void testFiles() throws IOException, ParserConfigurationException, SAXException {
		ContentParser parser = new ContentParser();
		Entry files = parser.parseFiles(DownloadTest.class.getResourceAsStream("Files.xml"));
		printEntry(files);
	}

	@Test
	public void readFile() throws IOException {
		try (
				InputStream is = DownloadTest.class.getResourceAsStream("4.bin.doc");
				//InputStream is = new FileInputStream("/home/asd/fgistp2/Республика Адыгея/3538b3d2-1b35-1709-e043-230019acb57c.zip")
		) {
			ContentReader.Content content = new ContentReader().read(is);
			System.out.println(content.toString());
			Assert.assertNotNull(content);
			Assert.assertTrue(content.getTotalSize() > 0);
			Assert.assertTrue(content.getNameSize() > 0);
			Assert.assertNotEquals(content.getName(), null);
			Assert.assertNotEquals(content.getBytes(), null);
			System.out.println(String.format(" %n, %n, %s", content));
		}
	}

}
