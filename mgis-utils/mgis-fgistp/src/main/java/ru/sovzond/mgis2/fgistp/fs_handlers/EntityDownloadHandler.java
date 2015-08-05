package ru.sovzond.mgis2.fgistp.fs_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;

import java.io.*;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public class EntityDownloadHandler implements Downloadable {
	private String baseDir = "./";

	public EntityDownloadHandler(String DOWNLOAD_DIR) {
		this.baseDir = DOWNLOAD_DIR;
	}

	@Override
	public void createDirs(List<Entry> entries) {
		String dirName = buildDirName(entries);
		new File(dirName).mkdirs();
	}

	private String buildDirName(List<Entry> entries) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseDir);
		for (Entry entry : entries) {
			if (entry.getProperties().containsKey("d:NAME")) {
				sb.append(entry.getProperties().get("d:NAME")).append("/");
			} else if (entry.getProperties().containsKey("d:FULL_DOC_NAME")) {
				sb.append(entry.getProperties().get("d:FULL_DOC_NAME")).append("/");
			}
		}
		return sb.toString();
	}

	@Override
	public void createFile(List<Entry> entries, Entry file, InputStream inputStream) throws IOException {
		String fileName = buildDirName(entries) + file.getProperties().get("d:TYPE_NAME");
		File targetFile = new File(fileName);
		try (OutputStream outStream = new FileOutputStream(targetFile)) {
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			outStream.write(buffer);
		}
	}
}
