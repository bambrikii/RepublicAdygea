package ru.sovzond.mgis2.fgistp.fs_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public interface Downloadable {
	void createDirs(List<Entry> entries);

	void createFile(List<Entry> entries, Entry file, InputStream inputStream) throws IOException;
}
