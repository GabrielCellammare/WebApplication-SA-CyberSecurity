package application.util;
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

import pannel.CustomMessage;

//import pannel.CustomMessage;

public class FileChecker {

public static boolean checkImageFile(Part filePart) throws IOException {
		
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			
			if ("jpeg".equals(fileExtension) || "jpg".equals(fileExtension) || "png".equals(fileExtension)) {
				
				long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB
				
				if (filePart.getSize() > maxSizeInBytes) {
					// Il file � troppo grande
					CustomMessage.showPanel("L'immagine selezionata supera la dimensione massima consentita che � di 5 MB");
					return false;

				}
				Tika tika = new Tika();
				String contentType = tika.detect(filePart.getInputStream());

				if (contentType != null && contentType.startsWith("image/")) {

				} else {

					CustomMessage.showPanel("Il file non � un'immagine valida.");
					return false;
				}
			} else {

				CustomMessage.showPanel("Estensione del file non supportata.");
				return false;
			}
		} else {
			CustomMessage.showPanel("Nessun file caricato");
			return false;

		}
		return true;
	}
}