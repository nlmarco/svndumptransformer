package co.codewizards.svndumptransformer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropContent implements Cloneable {

	private Map<ByteArray, ByteArray> properties = new LinkedHashMap<>();

	public PropContent() {
	}

	public PropContent(byte[] data) throws IOException {
		@SuppressWarnings("resource") // it's a byte-array, stupid!
		ExtDataInputStream in = new ExtDataInputStream(new ByteArrayInputStream(data));

		String line = in.readUtf8Line();
		while (! in.isEof()) {
			if (! line.startsWith("K "))
				throw new IOException("line does not start with 'K '!");

			int keyLength = getKeyOrValueLength(line);
			byte[] key = new byte[keyLength];
			in.readFully(key);

			int v = in.read();
			if (v != '\n')
				throw new IOException("Expected '\n', but found: " + v);

			line = in.readUtf8Line();
			if (! line.startsWith("V "))
				throw new IOException("line does not start with 'V '!");

			int valueLength = getKeyOrValueLength(line);
			byte[] value = new byte[valueLength];
			in.readFully(value);

			v = in.read();
			if (v != '\n')
				throw new IOException("Expected '\n', but found: " + v);

			properties.put(ByteArray.fromByteArray(key), ByteArray.fromByteArray(value));

			line = in.readUtf8Line();
		}

		if (! line.equals("PROPS-END\n"))
			throw new IOException("PropContent misaligned?! text does not end with 'PROPS-END\n'!");
	}

	private int getKeyOrValueLength(String keyOrValueHeader) throws IOException {
		String s = keyOrValueHeader;
		if (s.startsWith("K ") || s.startsWith("V "))
			s = s.substring(2);
		else
			throw new IOException("line does neither start with 'K ' nor with 'V '!");

		if (s.endsWith("\n"))
			s = s.substring(0, s.length() - 1);

		int result = Integer.parseInt(s);
		return result;
	}

	public Map<ByteArray, ByteArray> getProperties() {
		return properties;
	}

	public ByteArray getProperty(final String string) {
		final ByteArray key = ByteArray.fromString(string);
		final ByteArray result = properties.get(key);
		return result;
	}

	public String getPropertyAsString(final String string) {
		final ByteArray ba = getProperty(string);
		if (ba == null)
			return null;

		return new String(ba.getData(), StandardCharsets.UTF_8);
	}

	public void setProperty(String keyString, String valueString) {
		final ByteArray key = ByteArray.fromString(keyString);
		final ByteArray value = ByteArray.fromString(valueString);
		properties.put(key, value);
	}

	@Override
	public PropContent clone()  {
		PropContent clone;
		try {
			clone = (PropContent) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		clone.properties = new LinkedHashMap<>(this.properties);
		return clone;
	}

	public byte[] getData() {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			@SuppressWarnings("resource") // byte-array, stupid!
			ExtDataOutputStream out = new ExtDataOutputStream(bout);
			for (Map.Entry<ByteArray, ByteArray> me : properties.entrySet()) {
				byte[] key = me.getKey().getData();
				byte[] value = me.getValue().getData();

				out.writeUtf8("K ");
				out.writeUtf8(Integer.toString(key.length));
				out.writeUtf8("\n");

				out.write(key);
				out.writeUtf8("\n");

				out.writeUtf8("V ");
				out.writeUtf8(Integer.toString(value.length));
				out.writeUtf8("\n");

				out.write(value);
				out.writeUtf8("\n");
			}

			out.writeUtf8("PROPS-END\n");

			return bout.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
