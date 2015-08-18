package ru.sovzond.mgis2.fgistp;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.EntityPersistHandler;
import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.http_handlers.Downloadable;
import ru.sovzond.mgis2.fgistp.http_handlers.Downloader;
import ru.sovzond.mgis2.fgistp.http_handlers.EntityDownloadHandler;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class DownloadApp {

	private Downloadable downloadHandler;
	private Persistable persistHandler;
	private ForkJoinPool executor = new ForkJoinPool(8);

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
		final List<Entry> parents2 = new ArrayList<>(parents);
		if (entry != null) {
			parents2.add(entry);
		}
		persistHandler.createDirs(parents2);

		Entry children = downloadHandler.downloadChildren(id);
		entry.getChildren().addAll(children.getEntries());
		persistHandler.saveDocumentFilesInfo(parents2, entry, "entry");

		// Download documents
		for (final Entry document : downloadHandler.downloadDocuments(id).getEntries()) {
			Entry files = downloadHandler.downloadDocumentFiles(document);
			document.getFiles().addAll(files.getEntries());
			persistHandler.saveDocumentFilesInfo(parents2, document, "files");
			executor.execute(() -> {
				try {
					System.out.println("downloadDocumentArchive: " + document.getProperties().get("d:ID"));
					downloadHandler.downloadDocumentArchive(parents2, document, persistHandler);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}

		// Download children
		for (final Entry child : children.getEntries()) {
			executor.execute(() -> {
				try {
					System.out.println("downloadRecursively: " + child.getProperties().get("d:ID"));
					downloadRecursively(parents2, child.getId());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		}
	}

	public static void main(String[] args) throws ParserConfigurationException, JAXBException, SAXException, IOException, InterruptedException {
		Persistable persistHandler = new EntityPersistHandler("/home/asd/fgistp3/");
		Downloadable downloadHandler = new EntityDownloadHandler();
		DownloadApp app = new DownloadApp(downloadHandler, persistHandler);
		app.downloadRecursively(new ArrayList<>(), "http://fgis.economy.gov.ru/Applications/FGIS_PROM/Strategis.Server.FGIS.DataService/FGISDataService.svc/KTDs(23366M)");
		app.await();
	}

	public void await() {
		executor.awaitQuiescence(1, TimeUnit.SECONDS);
	}
}
