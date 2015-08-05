package ru.sovzond.mgis2.fgistp.fs_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.model.ExceptionEntry;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public interface Persistable {
	void createDirs(List<Entry> entries) throws JAXBException;

	void saveDocumentArchive(List<Entry> entries, Entry targetFileName, InputStream inputStream) throws JAXBException;

	void saveErrorInfo(List<Entry> entries, Entry file, ExceptionEntry exceptionEntry) throws JAXBException;

	void saveDocumentFilesInfo(List<Entry> parents2, Entry document, String category) throws JAXBException;
}
