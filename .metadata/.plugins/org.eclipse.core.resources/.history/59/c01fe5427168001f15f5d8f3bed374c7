package application.util.fileChecker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;

import org.apache.tika.Tika;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import application.util.customMessage.DisplayMessage;

public class ProposalChecker {

	public static boolean checkProposalFile(Part filePart, ServletContext context) throws IOException {
		// Controlla se il file è stato effettivamente caricato
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
				// L'estensione del file non è ".txt"
				DisplayMessage.showPanel("Puoi caricare solo file di testo in formato txt!");
			}
		} else {
			// Nessun file caricato
			DisplayMessage.showPanel("Devi caricare una proposta progettuale!");
		}
		// Se si arriva qui, qualcosa è andato storto, restituisci false
		return false;
	}

	/**
	public static String processFile(Part filePart) {

		try(InputStream inputstream = filePart.getInputStream()) {
			long maxSizeInBytes = 20 * 1024 * 1024;
			if (filePart.getSize() > maxSizeInBytes) {
				DisplayMessage.showPanel(
						"Il file supera la dimensione massima consentita. Il file puo' essere massimo di 20 MB");
				return null;
			}

			// Controlla content-type con Tika
			Tika tika = new Tika();
			String contentType = tika.detect(inputstream);
		} catch (IOException e) {
			e.printStackTrace();
			DisplayMessage.showPanel("C'è stato un problema con il caricamento del file!");

			// Restituisci null in caso di eccezione
			return null;
		}
		if ("text/plain".equals(contentType) || "text/html".equals(contentType)) {

			try(InputStream fileContent = filePart.getInputStream()){
				// Leggi il contenuto del filePart
				byte[] contentBytes = new byte[fileContent.available()];
				inputstream.read(contentBytes);
				String content = new String(contentBytes, StandardCharsets.UTF_8);
				// Rimuovi gli script JavaScript, compreso il testo all'interno

				Document document = Jsoup.parse(content);
				document.select("script, [type=application/javascript], [type=text/javascript]").remove();
				document.select("[text]").unwrap(); // Rimuovi anche il testo all'interno dei tag script

				document.select("[onclick]").removeAttr("onclick");
				document.select("[onload]").removeAttr("onload");

				String cleanedHtml = document.toString();

				return cleanedHtml;
			}catch (IOException e) {
				e.printStackTrace();
				DisplayMessage.showPanel("C'è stato un problema con il caricamento del file!");

				// Restituisci null in caso di eccezione
				return null;
			}
		}
		else {
			DisplayMessage.showPanel("Il file contiene del testo non valido!");

			// Restituisci null quando il file non è valido
			return null;
		}

	}
	*/
	
	public static String processFile(Part filePart) {

	    long maxSizeInBytes = 10 * 1024 * 1024;
	    if (filePart.getSize() > maxSizeInBytes) {
	        DisplayMessage.showPanel(
	                "Il file supera la dimensione massima consentita. Il file puo' essere massimo di 10 MB");
	        return null;
	    }

	    // Usa try-with-resources per gestire la chiusura dell'input stream
	    try (InputStream inputStream = filePart.getInputStream()) {
	        // Controlla il content-type con Tika
	        Tika tika = new Tika();
	        String contentType = tika.detect(inputStream);
	        System.out.println(contentType);

	        // Riposizionare l'InputStream se necessario (ricreare perché tika.detect() consuma inputStream)
	        if ("text/plain".equals(contentType) || "text/html".equals(contentType)) {
	            // Crea un nuovo InputStream per leggere di nuovo i dati
	            try (InputStream fileContentStream = filePart.getInputStream()) {
	                byte[] contentBytes = fileContentStream.readAllBytes();
	                String content = new String(contentBytes, StandardCharsets.UTF_8);

	                // Usa Jsoup per rimuovere gli script JavaScript
	                Document document = Jsoup.parse(content);
	                document.select("script, [type=application/javascript], [type=text/javascript]").remove();
	                document.select("[text]").unwrap(); // Rimuove anche il testo all'interno dei tag script

	                document.select("[onclick]").removeAttr("onclick");
	                document.select("[onload]").removeAttr("onload");

	                String cleanedHtml = document.toString();

	                return cleanedHtml;
	            }
	        } else {
	            DisplayMessage.showPanel("Il file contiene del testo non valido!");
	            return null;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        DisplayMessage.showPanel("C'è stato un problema con il caricamento del file!");
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
}

