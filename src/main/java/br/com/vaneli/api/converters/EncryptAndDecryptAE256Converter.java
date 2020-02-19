package br.com.vaneli.api.converters;

import br.com.vaneli.api.services.CryptoService;
import javax.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptAndDecryptAE256Converter implements AttributeConverter<String, String> {


  private final CryptoService cryptoService;

  public EncryptAndDecryptAE256Converter(
    CryptoService cryptoService) {
    this.cryptoService = cryptoService;
  }


  @Override
  public String convertToDatabaseColumn(String s) {
    return this.cryptoService.encryptAES256(s);
  }

  @Override
  public String convertToEntityAttribute(String s) {
    return this.cryptoService.decryptAES256(s);
  }

}
