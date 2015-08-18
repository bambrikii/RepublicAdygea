package ru.sovzond.mgis2.fgistp.http_handlers;

import org.xml.sax.SAXException;
import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.model.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public interface Downloadable {
	Entry downloadDocuments(String id) throws IOException, ParserConfigurationException, SAXException;

	Entry downloadChildren(String id) throws IOException, ParserConfigurationException, SAXException;

	Entry downloadDocumentFiles(Entry entry) throws IOException, ParserConfigurationException, SAXException;

	void downloadDocumentArchive(List<Entry> parents, Entry file, Persistable persistHandler) throws JAXBException;
}
