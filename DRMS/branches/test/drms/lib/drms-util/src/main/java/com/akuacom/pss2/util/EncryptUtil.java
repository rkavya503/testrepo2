package com.akuacom.pss2.util;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import org.apache.commons.codec.binary.Hex;

public class EncryptUtil {

	/**
	 * String to hold name of the encryption algorithm.
	 */
	public static final String ALGORITHM = "RSA";

	// get an RSA cipher object and print the provider
	private Cipher cipher;

	/**
	 * Encrypt the plain text using public key.
	 * 
	 * @param text
	 * @param inputStream
	 * @return
	 */

	public String encrypt(String text, InputStream inputStream) {
		String cipherText = null;
		ObjectInputStream keyinputStream = null;
		try {

			keyinputStream = new ObjectInputStream(inputStream);
			final PublicKey publicKey = (PublicKey) keyinputStream.readObject();

			// get an RSA cipher object and print the provider
			this.cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key
			this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] bytes = text.getBytes("UTF-8");

			byte[] encrypted = blockCipher(bytes, Cipher.ENCRYPT_MODE);

			char[] encryptedTranspherable = Hex.encodeHex(encrypted);
			cipherText = new String(encryptedTranspherable);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	/**
	 * Decrypt text using private key.
	 * 
	 * @param encrypted
	 * @param inputStream
	 * @return
	 */
	public String decrypt(String encrypted, PrivateKey privateKey) {
		String dectyptedText = null;
		try {
			// Decrypt the cipher text using the private key.
			// get an RSA cipher object and print the provider
			this.cipher = Cipher.getInstance(ALGORITHM);
			// decrypt the text using the private key
			this.cipher.init(Cipher.DECRYPT_MODE, privateKey);

			byte[] bts = Hex.decodeHex(encrypted.toCharArray());

			byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);

			dectyptedText = new String(decrypted, "UTF-8");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dectyptedText;
	}

	/**
	 * As only byte[] can be encrypted and decrypted this is used to convert
	 * string to byte[]
	 * 
	 * @param bytes
	 * @param mode
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private byte[] blockCipher(byte[] bytes, int mode)
			throws IllegalBlockSizeException, BadPaddingException {
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128
		// byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified
		// in this step
		byte[] buffer = new byte[length];

		for (int i = 0; i < bytes.length; i++) {

			// if we filled our buffer array we have our block ready for de- or
			// encryption
			if ((i > 0) && (i % length == 0)) {
				// execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn, scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the
				// bytes array we shorten it.
				if (i + length > bytes.length) {
					newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i % length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen
		// when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot"
		// the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn, scrambled);

		return toReturn;
	}

	private byte[] append(byte[] prefix, byte[] suffix) {
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}

}