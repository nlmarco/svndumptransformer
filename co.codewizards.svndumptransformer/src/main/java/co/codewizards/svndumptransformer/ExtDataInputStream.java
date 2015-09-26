package co.codewizards.svndumptransformer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ExtDataInputStream extends DataInputStream {

	public ExtDataInputStream(InputStream in) {
		super(in);
	}

	public String readUtf8Line() throws IOException {
		return readUtf8Until('\n');
	}

	public String readUtf8Until(int stopByte) throws IOException {
		byte[] bytes = readUntil(stopByte);
		String result = new String(bytes, StandardCharsets.UTF_8);
		return result;
	}

	/**
	 * Reads until INCLUDING the given {@code stopByte}.
	 * @param stopByte the byte at which to stop reading.
	 * @return the data read from the current position until including the {@code stopByte}. If the EOF is encountered before, the
	 * {@code stopByte} will be missing in the result.
	 * @throws IOException if reading failed.
	 */
	public byte[] readUntil(int stopByte) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int byteRead;
		while ((byteRead = in.read()) >= 0) {
			buffer.write(byteRead);

			if (byteRead == stopByte)
				break;
		}
		return buffer.toByteArray();
	}

	public boolean isEof() throws IOException {
		in.mark(1);
		int read = in.read();
		in.reset();
		return read < 0;
	}
}
