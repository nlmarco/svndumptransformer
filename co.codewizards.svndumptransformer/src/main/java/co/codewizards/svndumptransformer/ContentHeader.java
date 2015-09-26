package co.codewizards.svndumptransformer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ContentHeader implements Cloneable {

	private int leadingEmptyLineQty;
	private Map<String, String> properties = new LinkedHashMap<>();

	public ContentHeader() {
	}

	public int getLeadingEmptyLineQty() {
		return leadingEmptyLineQty;
	}
	public void setLeadingEmptyLineQty(int leadingEmptyLineQty) {
		this.leadingEmptyLineQty = leadingEmptyLineQty;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void addPropertyFromLine(String line) {
		if (line.endsWith("\n"))
			line = line.substring(0, line.length() - 1);

		final String separator = ": ";
		int separatorIndex = line.indexOf(separator);
		if (separatorIndex < 0)
			throw new IllegalArgumentException(String.format("line '%s' does not contain separator '%s'!", line, separator));

		String key = line.substring(0, separatorIndex);
		String value = line.substring(separatorIndex + separator.length());
		properties.put(key, value);
	}

	public int getPropContentLength() {
		String string = properties.get("Prop-content-length");
		if (string == null || string.isEmpty())
			return -1;

		int result = Integer.parseInt(string);
		return result;
	}

	public void setPropContentLength(int length) {
		properties.put("Prop-content-length", Integer.toString(length));
	}

	public int getContentLength() {
		String string = properties.get("Content-length");
		if (string == null || string.isEmpty())
			return -1;

		int result = Integer.parseInt(string);
		return result;
	}

	public void setContentLength(int length) {
		properties.put("Content-length", Integer.toString(length));
	}

	public int getTextContentLength() {
		String string = properties.get("Text-content-length");
		if (string == null || string.isEmpty())
			return -1;

		int result = Integer.parseInt(string);
		return result;
	}

	public void setTextContentLength(int length) {
		properties.put("Text-content-length", Integer.toString(length));
	}

	public int getRevisionNumber() {
		String string = properties.get("Revision-number");
		if (string == null || string.isEmpty())
			return -1;

		int result = Integer.parseInt(string);
		return result;
	}

	public String getNodePath() {
		String string = properties.get("Node-path");
		return string;
	}

	public void setNodePath(String string) {
		properties.put("Node-path", string);
	}

	public String getNodeCopyFromPath() {
		String string = properties.get("Node-copyfrom-path");
		return string;
	}

	public void setNodeCopyFromPath(String string) {
		properties.put("Node-copyfrom-path", string);
	}

	public UUID getUuid() {
		String string = properties.get("UUID");
		if (string == null || string.isEmpty())
			return null;

		return UUID.fromString(string);
	}

	public void setUuid(UUID uuid) {
		properties.put("UUID", uuid.toString());
	}

	@Override
	public ContentHeader clone() {
		ContentHeader clone;
		try {
			clone = (ContentHeader) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		clone.properties = new LinkedHashMap<>(this.properties);
		return clone;
	}
}
