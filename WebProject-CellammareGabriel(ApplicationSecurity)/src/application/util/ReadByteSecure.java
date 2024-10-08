package application.util;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.concurrent.Immutable;

import application.util.cryptography.PasswordManager;

@Immutable
public final class ReadByteSecure{

	//lettura di byte da uno stream di input per l'immutabilitÓ dovuta
	public static byte[] readAllBytesSecurely(InputStream inputStream) throws IOException {	
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    byte[] data = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, bytesRead);
	    }
	    PasswordManager.clearBytes(data);
	    return buffer.toByteArray();
	}
}
