package net.exchangenetwork.schema.icis;

import org.w3c.dom.ls.LSInput;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author dfladung
 * 
 */
public class XmlResolverInput implements LSInput {

	private String publicId;

	private String systemId;

	private BufferedInputStream inputStream;

	public XmlResolverInput(String publicId, String sysId, InputStream input) {
		this.publicId = publicId;
		this.systemId = sysId;
		this.inputStream = new BufferedInputStream(input);
	}

	public BufferedInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(BufferedInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getBaseURI() {
		return null;
	}

	public InputStream getByteStream() {
		return null;
	}

	public boolean getCertifiedText() {
		return false;
	}

	public Reader getCharacterStream() {
		return null;
	}

	public String getEncoding() {
		return null;
	}

	public String getStringData() {
		synchronized (inputStream) {
			try {
				byte[] input = new byte[inputStream.available()];
				inputStream.read(input);
				String contents = new String(input);
				return contents;
			} catch (IOException e) {
				System.out.println(e.getClass().getName() + ": " + e.getMessage());
				System.out.println("When resolving " + systemId);
				return null;
			}
		}
	}

	public void setBaseURI(String baseURI) {
	}

	public void setByteStream(InputStream byteStream) {
	}

	public void setCertifiedText(boolean certifiedText) {
	}

	public void setCharacterStream(Reader characterStream) {
	}

	public void setEncoding(String encoding) {
	}

	public void setStringData(String stringData) {
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
