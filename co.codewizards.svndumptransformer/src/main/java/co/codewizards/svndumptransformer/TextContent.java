package co.codewizards.svndumptransformer;

/**
 * Actual payload. Though it's called "TextContent", it does not necessarily contain text! It may be binary!
 * @author Marco หงุ่ยตระกูล-Schulze - marco at codewizards dot co
 */
public class TextContent implements Cloneable {

	private byte[] data;

	public TextContent() {
	}

	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	protected TextContent clone() {
		TextContent clone;
		try {
			clone = (TextContent) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		if (this.data != null) {
			clone.data = new byte[this.data.length];
			System.arraycopy(this.data, 0, clone.data, 0, this.data.length);
		}

		return clone;
	}
}
