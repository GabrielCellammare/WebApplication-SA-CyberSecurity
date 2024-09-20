package application.util.entity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import javax.annotation.concurrent.NotThreadSafe;

import application.util.ConvertingType;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;

@NotThreadSafe
public final class UserLogged {
	
	/**
	 * Classe che rappresenta l'utente loggato, che viene inizializzato con l'email crittografata
	 * Timestamp del momento in cui viene effettuato il login
	 * Token csrf per evitare attacchi CSRF
	 * Cookie token per effettuare registrazione
	 */

	private final byte[] byte_encryptedEmail;
	private final byte[] timestamp;
	private byte[] csrfToken; //Base64 byte encoding
	private final byte[] cookieToken; //Base64 byte encoding


	public UserLogged(byte[] byte_encryptedEmail){
		this.byte_encryptedEmail=byte_encryptedEmail;
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timestamp = this.calculateTimeStamp(ConvertingType.stringToByteArray(currentDateTime.format(formatter)));	
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		this.cookieToken=this.generateFinalToken(this.generateSecureCookieToken());
	}
	
	public UserLogged(byte[] byte_encryptedEmail, byte[] cookieToken){
		this.byte_encryptedEmail=byte_encryptedEmail;
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timestamp = this.calculateTimeStamp(ConvertingType.stringToByteArray(currentDateTime.format(formatter)));	
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		this.cookieToken=cookieToken;
	}

	/**
	 * Funzione privata per generazione csrf token
	 * @return
	 */
	private byte[] generateCsrfToken() {

		final int CSRF_TOKEN_LENGTH = 32; // 32 bytes * 8 = 256 bits
		final SecureRandom secureRandom = new SecureRandom();
		byte[] tokenBytes = new byte[CSRF_TOKEN_LENGTH];
		secureRandom.nextBytes(tokenBytes);
		byte[] tokenBytesPadding = Encryption.addPadding(tokenBytes);
		try{
			PasswordManager.clearBytes(tokenBytes);
			return Encryption.encrypt(tokenBytesPadding);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Funzione per generazione del cookie token
	 * @return
	 */
	private byte[] generateSecureCookieToken() {
		byte[] concatenatedData = new byte[this.byte_encryptedEmail.length + this.timestamp.length];

		System.arraycopy(this.byte_encryptedEmail, 0, concatenatedData, 0, this.byte_encryptedEmail.length);

		System.arraycopy(this.timestamp, 0, concatenatedData, this.byte_encryptedEmail.length, this.timestamp.length);
		
		byte[] concatenedDataPadding = Encryption.addPadding(concatenatedData);
		
		byte[] cookieEncrypted = null;
		try {
			cookieEncrypted = Encryption.encrypt(concatenedDataPadding);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return PasswordManager.concatenateAndHash(cookieEncrypted,this.csrfToken);

	}
	
	/**
	 * Funzione per la generazione del token finale, concatenato con la mail cifrata
	 * @param token
	 * @return
	 */
	private byte[] generateFinalToken(byte[] token) {
		
		return Base64.getEncoder().encode(PasswordManager.concatenateAndHash(this.byte_encryptedEmail, token));

	}
	
	/**
	 * Calcolo del timestamp, risultato dell'hash con il timestamp e byte randomici
	 * @param timestamp
	 * @return
	 */
	private byte[] calculateTimeStamp(byte[] timestamp) {
		
		return PasswordManager.concatenateAndHash(timestamp, PasswordManager.generateRandomBytes(timestamp.length));
		
	}

	public byte[] getByte_encryptedEmail() {
		return this.byte_encryptedEmail;
	}

	public byte[] getTimestamp() {
		return this.timestamp;
	}

	public byte[] getCsrfToken() {
		return this.csrfToken;
	}

	public byte[] getCookieToken() {
		return this.cookieToken;
	}

	public void setCsrfToken() {
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		DisplayMessage.showPanel("Nuovo csrf token generato");
	}
	

}
