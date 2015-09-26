package co.codewizards.svndumptransformer;

import static co.codewizards.svndumptransformer.Util.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteArray {

	private final byte[] data;
	private final String string;
	private final int hashCode;

	private ByteArray(final byte[] data) {
		assertNotNull("data", data);
		this.data = data;
		this.string = new String(data, StandardCharsets.UTF_8);
		this.hashCode = Arrays.hashCode(data);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final ByteArray other = (ByteArray) obj;
		return Arrays.equals(data, other.data);
	}

	@Override
	public String toString() {
		return String.format("%s[%s, '%s']", this.getClass().getSimpleName(), encodeHexStr(data), string);
	}

	public static ByteArray fromString(String string) {
		assertNotNull("string", string);
		return new ByteArray(string.getBytes(StandardCharsets.UTF_8));
	}

	public static ByteArray fromByteArray(byte[] data) {
		return new ByteArray(data);
	}

	public byte[] getData() {
		return data;
	}
}
