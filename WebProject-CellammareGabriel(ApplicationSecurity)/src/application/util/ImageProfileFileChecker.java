package application.util;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.http.Part;
import org.apache.tika.Tika;

import application.util.customMessage.DisplayMessage;




public class ImageProfileFileChecker {
	
	private final static long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB

	public static boolean checkImageFile(Part filePart) throws IOException{

		if (filePart != null && filePart.getSize() > 0) {
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

			if ("jpeg".equals(fileExtension) || "jpg".equals(fileExtension) || "png".equals(fileExtension)) {

				if (filePart.getSize() > maxSizeInBytes) {
					DisplayMessage.showPanel("L'immagine selezionata supera la dimensione massima consentita che � di 5 MB");
					return false;

				}
				Tika tika = new Tika();
				String contentType = tika.detect(filePart.getInputStream());

				if (contentType != null && contentType.startsWith("image/")) {

				} else {

					DisplayMessage.showPanel("Il file non � un'immagine valida.");
					return false;
				}
				
				
			} else {

				DisplayMessage.showPanel("Estensione del file non supportata.");
				return false;
			}
			
			
		} else {
			DisplayMessage.showPanel("Nessun file caricato");
			return false;

		}
		return true;
	}


}
