package application.util.fileChecker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tika.Tika;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import application.util.ConvertingType;
import application.util.ReadByteSecure;
import application.util.cryptography.Encryption;
import application.util.cryptography.PasswordManager;
import application.util.customMessage.DisplayMessage;
import model.Dao.cookie.DeleteTokenDAO;

public final class ProposalChecker {

	public static boolean checkProposalFile(HttpServletRequest request, HttpServletResponse response, HttpSession session,Part filePart, ServletContext context, byte[] checksumOriginal) throws IOException {
		
		
		byte[] lastChecksum = Encryption.calculateChecksumFromPart(filePart);
		
		ProposalChecker.checksumControl(request, response, session, checksumOriginal, lastChecksum);
		
		System.out.println("Checksum 2: " + lastChecksum.toString());
		

		PasswordManager.clearBytes(lastChecksum);
		
		// Controlla se il file � stato effettivamente caricato
		if (filePart != null && filePart.getSize() > 0) {
			// Ottieni il nome del file
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

			System.out.println("fileName: " + fileName);
			// Controlla l'estensione del file
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if ("txt".equals(fileExtension)) {

				String realPath = context.getRealPath("/");
				Path filePath = Paths.get(realPath, fileName);
				System.out.println("filePath: " + filePath);

				return true;
			} else {
				// L'estensione del file non � ".txt"
				DisplayMessage.showPanel("Puoi caricare solo file di testo in formato txt!");
			}
		} else {
			// Nessun file caricato
			DisplayMessage.showPanel("Devi caricare una proposta progettuale!");
		}
		// Se si arriva qui, qualcosa � andato storto, restituisci false
		return false;
	}
	
	public static String processFile(HttpServletRequest request, HttpServletResponse response, HttpSession session,Part filePart,byte[] checksumOriginal) {
		
		
	    long maxSizeInBytes = 10 * 1024 * 1024; //Max 10MB
	    if (filePart.getSize() > maxSizeInBytes) {
	        DisplayMessage.showPanel(
	                "Il file supera la dimensione massima consentita. Il file puo' essere massimo di 10 MB");
	        return null;
	    }

	    // Usa try-with-resources per gestire la chiusura dell'input stream
	    try (InputStream inputStream = filePart.getInputStream()) {
			
			byte[] lastChecksum = Encryption.calculateChecksumFromPart(filePart);
			
			ProposalChecker.checksumControl(request, response, session, checksumOriginal, lastChecksum);

			System.out.println("Checksum 3: " + lastChecksum.toString());
			PasswordManager.clearBytes(lastChecksum);
			
			
	        // Controlla il content-type con Tika
	        Tika tika = new Tika();
	        String contentType = tika.detect(inputStream);
	        System.out.println(contentType);

	        // Riposizionare l'InputStream se necessario (ricreare perch� tika.detect() consuma inputStream)
	        if ("text/plain".equals(contentType) || "text/html".equals(contentType)) {
	            // Crea un nuovo InputStream per leggere di nuovo i dati
	            try (InputStream fileContentStream = filePart.getInputStream()) {
	                byte[] contentBytes = ReadByteSecure.readAllBytesSecurely(fileContentStream);

	                // Usa Jsoup per rimuovere gli script JavaScript
	                Document document = Jsoup.parse(ConvertingType.byteArrayToString(contentBytes)); //aggiunge i tag
	                document.select("script, [type=application/javascript], [type=text/javascript]").remove();
	                document.select("[text]").unwrap(); // Rimuove anche il testo all'interno dei tag script

	                document.select("[onclick]").removeAttr("onclick");
	                document.select("[onload]").removeAttr("onload");

	                String cleanedHtml = document.toString();
	                
	                checksumOriginal = Encryption.calculateChecksumFile(cleanedHtml.getBytes(StandardCharsets.UTF_8));
	                System.out.println("Checksum 4: " + checksumOriginal.toString());
	                return cleanedHtml;
	            }
	        } else {
	            DisplayMessage.showPanel("Il file contiene del testo non valido!");
	            return null;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        DisplayMessage.showPanel("C'� stato un problema con il caricamento del file!");
	        return null;
	    }
	}


	private static String getFileNameFromPath(String filePath) {
		Path path = Paths.get(filePath);
		return path.getFileName().toString();
	}

	public static String getFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String[] tokens = contentDisposition.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				// Ottieni solo il nome del file dal percorso completo
				String fileNameWithExtension = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
				return getFileNameFromPath(fileNameWithExtension);
			}
		}
		return "";
	}	
	
	
	public static void checksumControl(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			byte[] checksumOriginalFile, byte[] lastChecksum) throws IOException {
		
		boolean check=Arrays.equals(checksumOriginalFile, lastChecksum);

		if(!check){
			if (session != null) {
				session.invalidate();
			}
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("rememberMe".equals(cookie.getName())) {
						byte[] cookieByte = Base64.getDecoder().decode(cookie.getValue()); 
						if(DeleteTokenDAO.deleteToken(cookieByte)) {
							// Rimuove il cookie dal browser
							cookie.setMaxAge(0);
							cookie.setHttpOnly(true);
							cookie.setSecure(true);
							response.addCookie(cookie);
							response.sendRedirect("userNotLoggedIndex.jsp");
							DisplayMessage.showPanel("Logout fozato effettuato correttamente!");
						}
						
					}
				}
			}
			
			PasswordManager.clearBytes(lastChecksum);
			PasswordManager.clearBytes(checksumOriginalFile);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			DisplayMessage.showPanel("Non � stato possibile caricare il file della proposta, i file sembrano diversi!");
		}else {
			DisplayMessage.showPanel("Nessun cambiamento rilevato durante il controllo del checksum della proposta");
		}
	}
	
}

