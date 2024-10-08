package application.util.fileChecker;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.http.Part;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import application.util.cryptography.Encryption;
import application.util.customMessage.DisplayMessage;

@ThreadSafe
public final class ImageProfileFileChecker {


	/**
	 * Metodo che controlla che il checksum sia corretto (Vulnerabilit� TOC-TOU), verifica la dimensione dell'immagine del profilo
	 * e che l'estensione del file sia quella dichiarata
	 * @param filePart
	 * @param checksumOriginal
	 * @return
	 * @throws IOException
	 */
	public static boolean checkImageFile(Part filePart, byte[] checksumOriginal) throws IOException{
		final long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB
		Tika tika = new Tika();
		boolean contentTypeBool=false;
		boolean check=false;
		
		byte[] lastChecksum = Encryption.calculateChecksumFromPart(filePart);

		check=Arrays.equals(checksumOriginal, lastChecksum);

		if(!check) {
			DisplayMessage.showPanel("Non � stato possibile terminare la registrazione, le immagini profilo non risultano uguali!");
			return false; 
		}

		if (filePart != null && filePart.getSize() > 0) {

			try {
				ImageProfileFileChecker.printMetadata(filePart, tika);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

			if ("jpeg".equals(fileExtension) || "jpg".equals(fileExtension) || "png".equals(fileExtension)) {

				if (filePart.getSize() > maxSizeInBytes) {
					DisplayMessage.showPanel("L'immagine selezionata supera la dimensione massima consentita che � di 5 MB");
					return false;

				}


				try(InputStream inputstream = filePart.getInputStream()){
					String contentType = tika.detect(inputstream);

					if (contentType != null && contentType.startsWith("image/")) {
						contentTypeBool=true;

					} else {

						contentTypeBool=false;
						DisplayMessage.showPanel("Il file non � un'immagine valida.");
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


	
	/**
	 * Metodo privato che si occupa della stampa di tutti i metadati del file correlato
	 * @param filePart
	 * @param tika
	 * @throws Exception
	 */
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