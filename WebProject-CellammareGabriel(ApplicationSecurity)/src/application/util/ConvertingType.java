package application.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ConvertingType {
	
	public static byte[] charToBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
				byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}

	public  static char[] parseStringtoCharArray(String str) {
		char[] arr = new char[str.length()]; 


		for (int i = 0; i < str.length(); i++) { 

			arr[i] = str.charAt(i); 
		} 

		return arr;

	}
	
	public  static String byteArrayToString(byte[] byteArray) {
		return new String(byteArray, StandardCharsets.UTF_8);
	}
	
	public  static byte[] stringToByteArray(String str) {
		return str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
	}

	public static boolean areCharArraysEqual(char[] array1, char[] array2) {
		if (array1 == null || array2 == null) {
			return array1 == array2;
		}
		if (array1.length != array2.length) {
			return false;
		}
		for (int i = 0; i < array1.length; i++) {
			if (array1[i] != array2[i]) {
				return false;
			}
		}
		return true;
	}

}