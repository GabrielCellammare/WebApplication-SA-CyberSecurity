package application.util.fileChecker;

import java.util.regex.Pattern;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class EmailChecker {
	
	public final static boolean isValidEmail(String email) {
		
		final Pattern ptr = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
		        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		return ptr.matcher(email).matches();
	}

}
