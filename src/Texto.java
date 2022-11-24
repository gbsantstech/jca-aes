import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Texto {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchPaddingException, IOException {

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecretKey secretKey = keyGenerator.generateKey();
		String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println("Chave gerada = " + secretKeyString);

		Cipher encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] InitVectorBytes = keyGenerator.generateKey().getEncoded();
		IvParameterSpec parameterSpec = new IvParameterSpec(InitVectorBytes);
		encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

		File file = new File(args[0]);
		Scanner sc = null;
		sc = new Scanner(file);

		while (sc.hasNextLine()) {
			String line = sc.nextLine();

			byte[] encryptedMessageBytes = encryptionCipher.doFinal(line.getBytes());
			String encryptedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
			System.out.println(encryptedMessage);
			
			FileWriter arquivoCripto = new FileWriter("aguasdemarco.cripto", true);
			BufferedWriter escrito = new BufferedWriter(arquivoCripto);
			escrito.write(encryptedMessage);
			escrito.newLine();
			escrito.close();
			arquivoCripto.close();

			Cipher decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
			byte[] decryptedMessageBytes = decryptionCipher.doFinal(encryptedMessageBytes);
			String decryptedMessage = new String(decryptedMessageBytes);


			FileWriter escreverArquivo = new FileWriter("aguasdemarcoemclaro.txt", true);
			BufferedWriter escrever = new BufferedWriter(escreverArquivo);
			escrever.write(decryptedMessage);
			escrever.newLine();
			escrever.close();
			escreverArquivo.close();
		

		}
	}

}
