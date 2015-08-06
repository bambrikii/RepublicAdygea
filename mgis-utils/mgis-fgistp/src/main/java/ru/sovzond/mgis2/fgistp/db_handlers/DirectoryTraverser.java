package ru.sovzond.mgis2.fgistp.db_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.sax_handlers.EntryMarshaller;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class DirectoryTraverser {
	public void go(String directoryName) throws IOException, JAXBException {
		System.out.println("Directory: " + directoryName);
		File file = new File(directoryName);
		File[] entries = file.listFiles(pathname -> pathname.getName().endsWith(".entry.xml"));
		if (entries != null) {
			for (File entry : entries) {
				try (FileInputStream is = new FileInputStream(entry)) {
					Entry entry1 = EntryMarshaller.unmarshall(is);
					System.out.println("Entry: " + entry1.toString());
					for (Entry child : entry1.getChildren()) {
						System.out.println("Child: " + entry1.toString());
						go(directoryName + child.getProperties().get("d:NAME"));
					}
				}
			}
		}
		File[] documents = file.listFiles(pathname -> pathname.getName().endsWith(".files.xml"));
		// TODO:
	}
}
