package application.util;

import java.util.regex.Pattern;

public final class EmailChecker {
	
	
	final static Pattern ptr = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
	
	public static boolean isValidEmail(String email) {
		return ptr.matcher(email).matches();
	}

}
