package ru.sovzond.mgis2.fgistp.http_handlers;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.ContentParser;
import ru.sovzond.mgis2.fgistp.Downloader;
import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.model.ExceptionEntry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public class EntityDownloadHandler implements Downloadable {
	private static final String BASE_URL = "http://fgis.economy.gov.ru/Applications/FGIS_PROM/";
	private static final String KTDS = "Strategis.Server.FGIS.DataService/FGISDataService.svc/KTDs{0}";
	private static final String GET_DOCUMENT_FILES = "Strategis.Server.FGIS.DataService/FGISDataService.svc/GetDocumentFiles()?$inlinecount=allpages&documentId=''{0}''";
	private static final String DOCUMENT_SERVICE = "Strategis.Server.FGIS.FileDownloadService/?documentId={0}&fileIds={1}&userId=1&isExport=False&isFileContent=False";

	public Entry downloadDocuments(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(id + "/DOCUMENTS")) {
			return new ContentParser().parseChildren(is);
		}
	}

	public Entry downloadChildren(String id) throws IOException, ParserConfigurationException, SAXException {
		try (InputStream is = new Downloader().doGet(id + "/Children")) {
			return new ContentParser().parseChildren(is);
		}
	}

	public Entry downloadDocumentFiles(Entry entry) throws IOException, ParserConfigurationException, SAXException {
		String id = entry.getProperties().get("d:ID");
		try (InputStream is = new Downloader().doGet(BASE_URL + MessageFormat.format(GET_DOCUMENT_FILES, id))) {
			return new ContentParser().parseFiles(is);
		}
	}



	public void downloadDocumentArchive(List<Entry> parents, Entry file, Persistable persistHandler) throws JAXBException {
		String documentId = file.getProperties().get("d:ID");
		String urlString = BASE_URL + MessageFormat.format(DOCUMENT_SERVICE, documentId, "");
		try (InputStream inputStream = new Downloader().doGet(urlString)) {
			persistHandler.saveDocumentArchive(parents, file, inputStream);
		} catch (Exception ex) {
			persistHandler.saveErrorInfo(parents, file, new ExceptionEntry(ex.getMessage()));
		}
	}
}
