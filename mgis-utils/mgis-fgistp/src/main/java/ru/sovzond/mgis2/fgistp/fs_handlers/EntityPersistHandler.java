package ru.sovzond.mgis2.fgistp.fs_handlers;

import ru.sovzond.mgis2.fgistp.model.Entry;
import ru.sovzond.mgis2.fgistp.model.ExceptionEntry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

/**
 * Created by Alexander Arakelyan on 05.08.15.
 */
public class EntityPersistHandler implements Persistable {
	private String baseDir = "./";

	public EntityPersistHandler(String DOWNLOAD_DIR) {
		this.baseDir = DOWNLOAD_DIR;
	}

	@Override
	public void createDirs(List<Entry> entries) throws JAXBException {
		String dirName = buildDirName(entries);
		new File(dirName).mkdirs();
		if (entries.size() > 0) {
			Entry lastEntry = entries.get(entries.size() - 1);
//			createResponseFile(dirName + ".response.xml", lastEntry);
		}
	}

//	private void createResponseFile(String fileName, Entry lastEntry) throws JAXBException {
//		JAXBContext context = JAXBContext.newInstance(Entry.class);
//		Marshaller m = context.createMarshaller();
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		m.marshal(lastEntry, new File(fileName));
//	}

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
	public void saveDocumentArchive(List<Entry> entries, Entry file, InputStream inputStream) throws JAXBException {
		String fileName0 = buildDirName(entries) + /*file.getProperties().get("d:FULL_DOC_NAME") + "." + */file.getProperties().get("d:ID");
		String targetFileName = fileName0 + ".zip";
		try (ReadableByteChannel rbc = Channels.newChannel(inputStream);
			 FileOutputStream fos = new FileOutputStream(targetFileName);
		) {
			ByteBuffer sizeBuff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			rbc.read(sizeBuff);
			sizeBuff.rewind();
			int totalSize = sizeBuff.getInt();
			int nameSize = sizeBuff.getShort();
			ByteBuffer nameBuffer = ByteBuffer.allocate(nameSize);
			rbc.read(nameBuffer);
			String name = new String(nameBuffer.array());
			FileChannel channel = fos.getChannel();
			int position = 8 + nameSize + 1;
			channel.transferFrom(rbc, 0, Long.MAX_VALUE);
			System.out.println(String.format("File complete: %d, %d, %s, %s", totalSize, nameSize, name, targetFileName));
		} catch (Exception ex) {
			ex.printStackTrace();
			ExceptionEntry exceptionEntry = new ExceptionEntry(ex.getMessage());
			createErrorFile(fileName0 + ".error.xml", exceptionEntry);
		}
	}

	@Override
	public void saveErrorInfo(List<Entry> entries, Entry file, ExceptionEntry exceptionEntry) throws JAXBException {
		String dirName = buildDirName(entries);
		String fileName = dirName + file.getProperties().get("d:NAME");
		createErrorFile(fileName, exceptionEntry);
	}

	@Override
	public void saveDocumentFilesInfo(List<Entry> entries, Entry document, String category) throws JAXBException {
		String fileName = buildDirName(entries) + /*document.getProperties().get("d:FULL_DOC_NAME") + "." + */document.getProperties().get("d:ID") + "." + category + ".xml";
		JAXBContext context = JAXBContext.newInstance(Entry.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(document, new File(fileName));
	}

	private void createErrorFile(String fileName, ExceptionEntry exceptionEntry) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ExceptionEntry.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(exceptionEntry, new File(fileName));
	}
}
