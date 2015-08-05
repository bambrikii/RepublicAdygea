package ru.sovzond.mgis2.fgistp;

import org.junit.Test;
import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.Downloadable;
import ru.sovzond.mgis2.fgistp.fs_handlers.EntityDownloadHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class DownloadTest {
	private static final String DOWNLOAD_DIR = "./downloads/";

	@Test
	public void testDownload() throws IOException, ParserConfigurationException, SAXException {
		Downloadable downloadHandler = new EntityDownloadHandler(DOWNLOAD_DIR);
		DownloadApp app = new DownloadApp(downloadHandler);
		app.download("(23366M)");
	}

	@Test
	public void testDownloadRecursive() throws IOException, ParserConfigurationException, SAXException {
		Downloadable downloadHandler = new EntityDownloadHandler(DOWNLOAD_DIR);
		DownloadApp app = new DownloadApp(downloadHandler);
		app.downloadRecursively(new ArrayList<>(), "(23366M)");
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

}
