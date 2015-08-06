package ru.sovzond.mgis2.fgistp.db_handlers;

import ru.sovzond.mgis2.fgistp.fs_handlers.Persistable;
import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.model.ExceptionEntry;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DbPersistenceHandler implements Persistable {
	@Override
	public void createDirs(List<Entry> entries) throws JAXBException {

	}

	@Override
	public void saveDocumentArchive(List<Entry> entries, Entry targetFileName, InputStream inputStream) throws JAXBException {

	}

	@Override
	public void saveErrorInfo(List<Entry> entries, Entry file, ExceptionEntry exceptionEntry) throws JAXBException {

	}

	@Override
	public void saveDocumentFilesInfo(List<Entry> parents2, Entry document, String category) throws JAXBException {

	}
}
