package application.util.entity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;

public class UserLogged {

	private byte[] byte_encryptedEmail;
	private byte[] timestamp;
	private byte[] csrfToken;
	private byte[] cookieToken;


	public UserLogged(byte[] byte_encryptedEmail){
		this.byte_encryptedEmail=byte_encryptedEmail;
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timestamp = this.calculateTimeStamp(currentDateTime.format(formatter).getBytes(java.nio.charset.StandardCharsets.UTF_8));	
		this.csrfToken=this.generateFinalToken(this.generateCsrfToken());
		this.cookieToken=this.generateFinalToken(this.generateSecureCookieToken());
	}
	
	public UserLogged(byte[] byte_encryptedEmail,byte[] cookieToken){
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
		byte[] tokenBytesPadding_encrypted=null;
		try{
			tokenBytesPadding_encrypted = Encryption.encrypt(tokenBytesPadding);
			return tokenBytesPadding_encrypted;
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
	}
 
	

}
