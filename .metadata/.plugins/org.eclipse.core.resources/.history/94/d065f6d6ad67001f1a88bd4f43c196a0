package application.util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.http.Part;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import application.util.customMessage.DisplayMessage;




public class ImageProfileFileChecker {

	private final static long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB

	public static boolean checkImageFile(Part filePart) throws IOException{

		Tika tika = new Tika();
		boolean contentTypeBool=false;

		if (filePart != null && filePart.getSize() > 0) {

			try {
				printMetadata(filePart, tika);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

			if ("jpeg".equals(fileExtension) || "jpg".equals(fileExtension) || "png".equals(fileExtension)) {

				if (filePart.getSize() > maxSizeInBytes) {
					DisplayMessage.showPanel("L'immagine selezionata supera la dimensione massima consentita che è di 5 MB");
					return false;

				}


				try(InputStream inputstream = filePart.getInputStream()){
					String contentType = tika.detect(inputstream);

					if (contentType != null && contentType.startsWith("image/")) {
						contentTypeBool=true;

					} else {

						DisplayMessage.showPanel("Il file non è un'immagine valida.");
						contentTypeBool=false;
					}
				}catch (IOException e) {
					e.printStackTrace();
				}

				return contentTypeBool;


			} else {

				DisplayMessage.showPanel("Estensione del file non supportata.");
				return false;
			}


		} else {
			DisplayMessage.showPanel("Nessun file caricato");
			return false;

		}

	}




	private static void printMetadata(Part filePart, Tika tika) throws Exception {

		try(InputStream inputstream = filePart.getInputStream()){

			String fileType = tika.detect(inputstream);
			System.out.println("Content Type: " + fileType);

			// Estrae i metadati
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			Parser parser = new AutoDetectParser();
			ParseContext parseContext = new ParseContext();

			parser.parse(inputstream, handler, metadata, parseContext);

			// Stampa i metadati
			String[] metadataNames = metadata.names();
			for (String name : metadataNames) {
				System.out.println(name + ": " + metadata.get(name));
			}

		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}