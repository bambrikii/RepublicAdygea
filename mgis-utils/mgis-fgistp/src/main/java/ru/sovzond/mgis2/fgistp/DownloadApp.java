package ru.sovzond.mgis2.fgistp;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.Downloadable;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class DownloadApp {

	private static final String BASE_URL = "http://fgis.economy.gov.ru/Applications/FGIS_PROM/";
	private static final String KTDS = "Strategis.Server.FGIS.DataService/FGISDataService.svc/KTDs{0}";
	private static final String GET_DOCUMENT_FILES = "Strategis.Server.FGIS.DataService/FGISDataService.svc/GetDocumentFiles()?$inlinecount=allpages&documentId=''{0}''";
	private static final String DOCUMENT_SERVICE = "Strategis.Server.FGIS.FileDownloadService/?documentId={0}&fileIds={1}&userId=1&isExport=False&isFileContent=False";
	private Downloadable downloadHandler;

	public DownloadApp(Downloadable downloadHandler) {
		this.downloadHandler = downloadHandler;
	}

	public Entry download(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(BASE_URL + MessageFormat.format(KTDS, id))) {
			return new ContentParser().parseEntry(is);
		}
	}

	public void downloadRecursively(List<Entry> parents, String id) throws IOException, ParserConfigurationException, SAXException {
		// Download current item
		Entry entry = download(id);
		List<Entry> parents2 = new ArrayList<>(parents);
		if (entry != null) {
			parents2.add(entry);
		}
		downloadHandler.createDirs(parents);
		// Download documents
		Entry documents = downloadDocuments(id);
		for (Entry document : documents.getEntries()) {
			List<Entry> parents3 = new ArrayList<>(parents2);
			parents3.add(document);
			downloadHandler.createDirs(parents3);
			Entry files = downloadDocumentFiles(document);
			for (Entry file : files.getEntries()) {
				downloadDocumentFile(parents2, file);
			}
		}
		Entry children = downloadChildren(id);
		for (Entry child : children.getEntries()) {
			downloadRecursively(parents2, child.getId());
		}
	}

	private Entry downloadDocuments(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(BASE_URL + MessageFormat.format(KTDS, id) + "/DOCUMENTS")) {
			return new ContentParser().parseChildren(is);
		}
	}

	private Entry downloadDocumentFiles(Entry entry) throws IOException, ParserConfigurationException, SAXException {
		String id = entry.getProperties().get("d:ID");
		try (InputStream is = new Downloader().doGet(BASE_URL + MessageFormat.format(GET_DOCUMENT_FILES, id))) {
			return new ContentParser().parseFiles(is);
		}

	}

	private void downloadDocumentFile(List<Entry> parents, Entry file) throws IOException {
		String documentId = file.getProperties().get("d:DOCUMENT_ID");
		String fileId = file.getProperties().get("d:ID");
		try (InputStream inputStream = new Downloader().doGet(BASE_URL + MessageFormat.format(DOCUMENT_SERVICE, documentId, fileId))) {
			downloadHandler.createFile(parents, file, inputStream);
		}
	}

	private Entry downloadChildren(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(BASE_URL + MessageFormat.format(KTDS, id) + "/Children")) {
			return new ContentParser().parseChildren(is);
		}
	}
}
