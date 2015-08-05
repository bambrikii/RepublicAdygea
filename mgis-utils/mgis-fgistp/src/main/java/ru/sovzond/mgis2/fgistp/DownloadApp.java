package ru.sovzond.mgis2.fgistp;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.http_handlers.Downloadable;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class DownloadApp {

	private Downloadable downloadHandler;
	private Persistable persistHandler;

	public DownloadApp(Downloadable downloadHandler, Persistable persistHandler) {
		this.downloadHandler = downloadHandler;
		this.persistHandler = persistHandler;
	}

	public Entry downloadEntry(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(id)) {
			return new ContentParser().parseEntry(is);
		}
	}

	public void downloadRecursively(List<Entry> parents, String id) throws IOException, ParserConfigurationException, SAXException, JAXBException {

		// Download current item
		Entry entry = downloadEntry(id);
		List<Entry> parents2 = new ArrayList<>(parents);
		if (entry != null) {
			parents2.add(entry);
		}
		persistHandler.createDirs(parents2);

		Entry children = downloadHandler.downloadChildren(id);
		entry.getChildren().addAll(children.getEntries());
		persistHandler.saveDocumentFilesInfo(parents2, entry, "entry");

		// Download documents
		for (Entry document : downloadHandler.downloadDocuments(id).getEntries()) {
			Entry files = downloadHandler.downloadDocumentFiles(document);
			document.getFiles().addAll(files.getEntries());
			persistHandler.saveDocumentFilesInfo(parents2, document, "files");
			downloadHandler.downloadDocumentArchive(parents2, document, persistHandler);
		}

		// Download children
		for (Entry child : children.getEntries()) {
			downloadRecursively(parents2, child.getId());
		}
	}

}
