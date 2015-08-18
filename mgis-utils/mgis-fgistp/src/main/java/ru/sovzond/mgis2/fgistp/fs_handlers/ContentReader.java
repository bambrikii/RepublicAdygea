package ru.sovzond.mgis2.fgistp.fs_handlers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Alexander Arakelyan on 06.08.15.
 */
public class ContentReader {

	public static class Content {
		private long totalSize;
		private int nameSize;
		private String name;
		private byte[] bytes;


		public long getTotalSize() {
			return totalSize;
		}

		public void setTotalSize(long totalSize) {
			this.totalSize = totalSize;
		}

		public int getNameSize() {
			return nameSize;
		}

		public void setNameSize(int nameSize) {
			this.nameSize = nameSize;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public String toString() {
			return totalSize + ", " + nameSize + ", " + name;
		}
	}

	public Content read(InputStream is) throws IOException {
		Content content = new Content();
		try (ReadableByteChannel rbc = Channels.newChannel(is);
			 FileOutputStream fos = new FileOutputStream("a.doc");
		) {
			FileChannel channel = fos.getChannel();

			ByteBuffer sizeBuff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			rbc.read(sizeBuff);
			sizeBuff.rewind();

			System.out.println("sizePos: " + sizeBuff.position());
			int totalSize = sizeBuff.getInt();

			int nameSize = sizeBuff.getShort();
			ByteBuffer nameBuffer = ByteBuffer.allocate(nameSize);
			rbc.read(nameBuffer);
			String name = new String(nameBuffer.array());

			channel.transferFrom(rbc, 0, Long.MAX_VALUE);

			content.setTotalSize(totalSize);
			content.setNameSize(nameSize);
			content.setName(name);
			content.setBytes("".getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return content;
	}

}
