package ru.sovzond.mgis2.fgistp.db_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;

import java.io.File;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class DocumentArchive {
	private Entry document;
	private File archive;

	public DocumentArchive(Entry document, File archive) {
		this.document = document;
		this.archive = archive;
	}

	public Entry getDocument() {
		return document;
	}

	public File getArchive() {
		return archive;
	}
}
