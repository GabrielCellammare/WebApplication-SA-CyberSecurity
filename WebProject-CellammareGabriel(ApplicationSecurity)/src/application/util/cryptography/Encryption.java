package application.util.cryptography;

import java.io.IOException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Part;

import application.util.ConvertingType;
import application.util.ReadByteSecure;
@Immutable
@ThreadSafe
public final class Encryption {

	public static byte[] encrypt(byte[] data) throws Exception {
		
		char[] AES_KEY=Encryption.readAesKey();
		SecretKey key = Encryption.getSecretKey(AES_KEY);

		if (key != null) {

			char [] AES_IV = Encryption.readAES_IV();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ConvertingType.charToBytes(AES_IV)));
			Arrays.fill(AES_KEY, '\0');
			Arrays.fill(AES_IV, '\0');
			return cipher.doFinal(data);
		} else {
			Arrays.fill(AES_KEY, '\0');
			System.out.println("Chiave non valida.");
			return null;
		}

	}

	public static byte[] decrypt(byte[] encryptedBytes) throws Exception {
		char[] AES_KEY=Encryption.readAesKey();
		SecretKey key = Encryption.getSecretKey(AES_KEY);
		if (key != null) {
			char[] AES_IV = Encryption.readAES_IV();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ConvertingType.charToBytes(AES_IV)));
			Arrays.fill(AES_KEY, '\0');
			Arrays.fill(AES_IV, '\0');
			return cipher.doFinal(encryptedBytes);
		} else {
			System.out.println("Chiave non valida.");
			Arrays.fill(AES_KEY, '\0');
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


	public static byte[] calculateChecksumFromPart(Part filePart) {
	    try (InputStream inputStream = filePart.getInputStream()) {
	        return calculateChecksumFile(ReadByteSecure.readAllBytesSecurely(inputStream));// Usa la funzione di checksum che accetta byte[]
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

		return ConvertingType.parseStringtoCharArray(appProperties.getProperty("aes.key"));
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

		return ConvertingType.parseStringtoCharArray(appProperties.getProperty("aes.iv"));
	}
	


	private static SecretKey getSecretKey(char[] AES_KEY) {

		return new SecretKeySpec(Base64.getDecoder().decode(String.copyValueOf(AES_KEY)), "AES");

	}
}	
