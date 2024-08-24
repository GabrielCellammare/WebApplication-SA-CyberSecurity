package application.util.cryptography;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	private static final String AES_KEY = aesKey();
	private static final String AES_IV = "1234567890123456"; // Cambia l'IV a tuo piacimento

	private static SecretKey secretKey;

	
	public static byte[] encrypt(byte[] data) throws Exception {
		SecretKey key = getSecretKey();
		if (key != null) {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(AES_IV.getBytes()));
			return cipher.doFinal(data);
		} else {
			System.out.println("Chiave non valida.");
			return null;
		}
	}

	public static byte[] decrypt(byte[] encryptedBytes) throws Exception {
		SecretKey key = getSecretKey();
		if (key != null) {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(AES_IV.getBytes()));
			return cipher.doFinal(encryptedBytes);
		} else {
			System.out.println("Chiave non valida.");
			return null;
		}
	}

	private static String aesKey() {
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

		return appProperties.getProperty("aes.key");
	}
	
	private static SecretKey getSecretKey() {
		if (secretKey == null) {
			secretKey = new SecretKeySpec(Base64.getDecoder().decode(AES_KEY), "AES");
		}
		return secretKey;
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

}