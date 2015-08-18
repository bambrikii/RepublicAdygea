package ru.sovzond.mgis2.fgistp.db_handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.sax_handlers.EntryMarshaller;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DirectoryTraverser {

	private static final Logger logger = LoggerFactory.getLogger(DirectoryTraverser.class);

	public static final String FILES_XML = ".files.xml";
	public static final String ENTRY_XML = ".entry.xml";
	public static final String ZIP = ".zip";
	private DatabasePersister persister;

	public DirectoryTraverser(DatabasePersister persister) {
		this.persister = persister;
	}

	public void go(String directoryName) throws IOException, JAXBException, ParseException, SQLException {
		System.out.println("Directory: " + directoryName);
		File file = new File(directoryName);
		File[] entriesFiles = file.listFiles(pathname -> pathname.getName().endsWith(ENTRY_XML));
		List<Entry> entries = new ArrayList<>();
		if (entriesFiles != null) {
			for (File entryFile : entriesFiles) {
				try (FileInputStream is = new FileInputStream(entryFile)) {
					Entry entry = EntryMarshaller.unmarshall(is);
					System.out.println("Entry: " + entry.toString());
					entries.add(entry);
					for (Entry child : entry.getChildren()) {
						System.out.println("Child: " + entry.toString());
						go(directoryName + child.getProperties().get("d:NAME"));
					}
				}
			}
		}
		File[] documentFiles = file.listFiles(pathname -> pathname.getName().endsWith(FILES_XML));
		List<DocumentArchive> documents = new ArrayList<>();
		// TODO:
		if (documentFiles != null) {
			for (File documentFile : documentFiles) {
				String archiveFileName = documentFile.getAbsolutePath().substring(0, documentFile.getAbsolutePath().length() - FILES_XML.length()) + ZIP;
				File archiveFile = new File(archiveFileName);
				if (archiveFile.exists()) {
					try (FileInputStream is = new FileInputStream(documentFile)) {
						Entry document = EntryMarshaller.unmarshall(is);
						documents.add(new DocumentArchive(document, archiveFile.getAbsoluteFile()));
					}
				} else {
					logger.error("Error: archive not found: " + archiveFileName);
				}
			}
		}
		persister.acquire(entries, documents);
	}
}
