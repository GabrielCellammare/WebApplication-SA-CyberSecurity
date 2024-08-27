package application.util.cryptography;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.Part;

public class PasswordManager {

	private final static SecureRandom secureRandom = new SecureRandom();
	private final static char[] specialCharacters = {'!','@','#','$','%','^','&','.','*','(',')','-','_','+','=','<','>','?','.'};

	public static void clearBytes(byte[] password) {
		if (password != null) {

			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
		}
	}

	public static byte[] concatenateAndHash(byte[] password, byte[] salt) {
		try {

			byte[] concatenatedData = new byte[password.length + salt.length];


			System.arraycopy(password, 0, concatenatedData, 0, password.length);


			System.arraycopy(salt, 0, concatenatedData, password.length, salt.length);


			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			return digest.digest(concatenatedData);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] generateRandomBytes(int saltLenghts) {


		byte[] salt = new byte[saltLenghts];
		secureRandom.nextBytes(salt);

		return salt;
	}

	public static boolean isStrongPassword(byte[] password) {

		boolean valueLenght = password.length >= 8;
		boolean hasUpperCase = false;
		boolean hasLowerCase = false;
		boolean hasDigit = false;
		boolean hasSpecialChar = false;

		for (byte b : password) {

			char c = (char) b;


			if (Character.isUpperCase(c)) {
				hasUpperCase = true;    
			}

			if (Character.isLowerCase(c)) {
				hasLowerCase = true;    
			}

			if (Character.isDigit(c)) {
				hasDigit = true;    
			}

			for (char specialChar: specialCharacters) {
				if(c==specialChar) {
					hasSpecialChar=true;
				}
			}

			c=' ';

		}
		
		return ((valueLenght) && (hasUpperCase) && (hasLowerCase) && (hasDigit) && (hasSpecialChar));

	}
	
	public static byte[] calculateChecksum(Part filePart){
		byte[] hashBytes=null;
        // Crea l'istanza MessageDigest per SHA-256
        MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Ottieni il flusso di input dal Part
        try (InputStream inputStream = filePart.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            // Leggi il flusso di input e aggiorna il digest
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Calcola l'hash
        hashBytes = digest.digest();
        return hashBytes;
       
    }
}
