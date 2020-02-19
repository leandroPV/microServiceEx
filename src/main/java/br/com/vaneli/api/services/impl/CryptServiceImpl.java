package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.UnprocessableEntityException;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.services.CryptoService;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CryptServiceImpl implements CryptoService {

  private final MessageError messageError;
  private final String strSecretKey;
  private final String salt;

  public CryptServiceImpl(
    MessageError messageError,
    @Value("${aes.secretKey}") String strSecretKey,
    @Value("${aes.salt}") String salt) {
    this.messageError = messageError;
    this.strSecretKey = strSecretKey;
    this.salt = salt;
  }

  /**
   * criptografa strToEncrypt com strSecretKey e salt
   *
   * @return strToEncrypt criptografado com strSecretKey e salt
   */
  @Override
  public String encryptAES256(String strToEncrypt) {

    try {
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(this.strSecretKey.toCharArray(), this.salt.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    } catch (Exception e) {
      log.error("Error while encrypting: " + e.getMessage());
      throw new UnprocessableEntityException(
        this.messageError.create(Messages.AES_ENCRYPT_ERROR), "Error at aes encrypting data.", e);
    }

  }

  /**
   * descriptografa strToDecrypt com strSecretKey e salt
   *
   * @return strToDecrypt descriptografado com strSecretKey e salt
   */
  @Override
  public String decryptAES256(String strToDecrypt) {

    try {
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      KeySpec spec = new PBEKeySpec(this.strSecretKey.toCharArray(), this.salt.getBytes(), 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      log.error("Error while decrypting: " + e.getMessage());
      throw new UnprocessableEntityException(
        this.messageError.create(Messages.AES_DECRYPT_ERROR), "Error at aes decrypting data.", e);
    }

  }

}
