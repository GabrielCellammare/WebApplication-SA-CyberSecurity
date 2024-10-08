package application.util.cryptography;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.annotation.concurrent.ThreadSafe;
@ThreadSafe
public final class PasswordManager {

	//Pulizia dall'array degli elementi
	public static void clearBytes(byte[] password) {
		if (password != null) {

			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
		}
	}

	//Calcolo dell'hash
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

	//Generazione sicura di byte
	public static byte[] generateRandomBytes(int saltLenghts) {

		SecureRandom secureRandom = new SecureRandom();
		byte[] salt = new byte[saltLenghts];
		secureRandom.nextBytes(salt);

		return salt;
	}

	
	//Controllo sicurezza password
	public static boolean isStrongPassword(byte[] password) {
		
		final char[] specialCharacters = {'!','@','#','$','%','^','&','.','*','(',')','-','_','+','=','<','>','?','.'};
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

}
