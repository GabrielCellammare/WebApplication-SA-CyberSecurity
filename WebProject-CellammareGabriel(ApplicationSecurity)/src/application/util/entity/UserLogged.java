package application.util.entity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import com.sun.istack.logging.Logger;

import application.util.ConvertingType;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;

public final class UserLogged {

	private byte[] byte_encryptedEmail;
	private byte[] timestamp;
	private byte[] csrfToken; //Base64 byte encoding
	private byte[] cookieToken; //Base64 byte encoding


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
		this.timestamp = this.calculateTimeStamp(currentDateTime.format(formatter).getBytes(java.nio.charset.StandardCharsets.UTF_8));	
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		this.cookieToken=cookieToken;
	}

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

	private byte[] generateSecureCookieToken() {
		byte[] concatenatedData = new byte[this.byte_encryptedEmail.length + this.timestamp.length];

		System.arraycopy(this.byte_encryptedEmail, 0, concatenatedData, 0, this.byte_encryptedEmail.length);

		System.arraycopy(this.timestamp, 0, concatenatedData, this.byte_encryptedEmail.length, this.timestamp.length);
		
		return PasswordManager.concatenateAndHash(concatenatedData,this.csrfToken);

	}
	
	
	private byte[] generateFinalToken(byte[] token) {
		
		return Base64.getEncoder().encode(PasswordManager.concatenateAndHash(this.byte_encryptedEmail, token));

	}
	
	private byte[] calculateTimeStamp(byte[] timestamp) {
		
		return PasswordManager.concatenateAndHash(timestamp, PasswordManager.generateRandomBytes(timestamp.length));
		
	}

	public byte[] getByte_encryptedEmail() {
		return byte_encryptedEmail;
	}

	public byte[] getTimestamp() {
		return timestamp;
	}

	public byte[] getCsrfToken() {
		return csrfToken;
	}

	public byte[] getCookieToken() {
		return cookieToken;
	}

	public void setCsrfToken() {
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		DisplayMessage.showPanel("Nuovo csrf token generato");
	}
 
	

}
