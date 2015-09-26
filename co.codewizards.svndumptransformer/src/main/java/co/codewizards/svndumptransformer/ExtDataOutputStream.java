package co.codewizards.svndumptransformer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExtDataOutputStream extends DataOutputStream {

	public ExtDataOutputStream(OutputStream out) {
		super(out);
	}

	public void writeUtf8(String string) throws IOException {
		final byte[] data = string.getBytes(StandardCharsets.UTF_8);
		out.write(data);
	}
}
