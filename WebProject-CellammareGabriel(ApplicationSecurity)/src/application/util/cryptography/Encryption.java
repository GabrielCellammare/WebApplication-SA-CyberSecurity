package application.util.cryptography;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Part;

public class Encryption {

	private static char[] readAesKey() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query AES - Aes.key");
			e.printStackTrace();
			return null;
		}

		return Encryption.parseStringtoCharArray(appProperties.getProperty("aes.key"));
	}

	private static char[] readAES_IV() {
		Properties appProperties = new Properties();

		try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.ini")) {
			if (input == null) {
				System.out.println("File di configurazione config.ini non trovato.");
				return null;
			}
			appProperties.load(input);
		} catch (IOException e) {
			System.out.println("Query AESIV - AesIV.key");
			e.printStackTrace();
			return null;
		}

		return Encryption.parseStringtoCharArray(appProperties.getProperty("aes.iv"));
	}

	private static SecretKey getSecretKey(char[] AES_KEY) {

		return new SecretKeySpec(Base64.getDecoder().decode(String.copyValueOf(AES_KEY)), "AES");

	}

	public static byte[] encrypt(byte[] data) throws Exception {
		char[] AES_KEY=Encryption.readAesKey();
		SecretKey key = Encryption.getSecretKey(AES_KEY);
		char [] AES_IV = Encryption.readAES_IV();
		if (key != null) {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Encryption.chartoBytes(AES_IV)));
			java.util.Arrays.fill(AES_KEY, '\0');
			java.util.Arrays.fill(AES_IV, '\0');
			return cipher.doFinal(data);
		} else {
			java.util.Arrays.fill(AES_KEY, '\0');
			java.util.Arrays.fill(AES_IV, '\0');
			System.out.println("Chiave non valida.");
			return null;
		}

	}

	public static byte[] decrypt(byte[] encryptedBytes) throws Exception {
		char[] AES_KEY=Encryption.readAesKey();
		SecretKey key = Encryption.getSecretKey(AES_KEY);
		char[] AES_IV = Encryption.readAES_IV();
		if (key != null) {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Encryption.chartoBytes(AES_IV)));
			java.util.Arrays.fill(AES_KEY, '\0');
			java.util.Arrays.fill(AES_IV, '\0');
			return cipher.doFinal(encryptedBytes);
		} else {
			System.out.println("Chiave non valida.");
			java.util.Arrays.fill(AES_KEY, '\0');
			java.util.Arrays.fill(AES_IV, '\0');
			return null;
		}


	}

	public static byte[] addPadding(byte[] bytes) {
		int paddingLength = 16 - bytes.length % 16;
		byte[] paddingBytes = new byte[paddingLength];
		Arrays.fill(paddingBytes, (byte) 0x00);

		return Arrays.copyOf(bytes, bytes.length + paddingLength);
	}

	public static byte[] removePadding(byte[] bytes) {
		int paddingValue = bytes[bytes.length - 1]; // Ottieni l'ultimo byte, che rappresenta il valore di padding
		int unpaddedLength = bytes.length - paddingValue;

		// Verifica se i byte finali sono effettivamente byte di padding con valore diverso da zero
		for (int i = unpaddedLength; i < bytes.length; i++) {
			if (bytes[i] != 0x00) {
				// I byte finali non sono tutti byte di padding, restituisci l'array originale
				return bytes;
			}
		}

		// Ritorna il nuovo array senza i byte di padding
		return Arrays.copyOf(bytes, unpaddedLength);
	}

	public static String byteArrayToString(byte[] byteArray) {
		return new String(byteArray, StandardCharsets.UTF_8);
	}


	public static char[] parseStringtoCharArray(String str) {
		char[] arr = new char[str.length()]; 


		for (int i = 0; i < str.length(); i++) { 

			arr[i] = str.charAt(i); 
		} 

		return arr;

	}

	public static byte[] calculateChecksumFromPart(Part filePart) {
	    try (InputStream inputStream = filePart.getInputStream()) {
	        byte[] fileContent = inputStream.readAllBytes();
	        return calculateChecksumFile(fileContent); // Usa la funzione di checksum che accetta byte[]
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public static byte[] calculateChecksumFile(byte[] fileContent){
		// Crea l'istanza MessageDigest per SHA-256
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		digest.update(fileContent);

		// Calcola l'hash
		String string = new String(digest.digest(), StandardCharsets.UTF_8);

		System.out.println(string); // Output: Hello
		return digest.digest();


	}


	private static byte[] chartoBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
				byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}
}	
