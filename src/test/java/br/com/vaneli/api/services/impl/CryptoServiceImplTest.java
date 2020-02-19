package br.com.vaneli.api.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import br.com.vaneli.api.exceptions.MessageError;
import br.com.vaneli.api.exceptions.MessageError.ApiError;
import br.com.vaneli.api.exceptions.UnprocessableEntityException;
import br.com.vaneli.api.interfaces.Messages;
import br.com.vaneli.api.services.CryptoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CryptoServiceImplTest {

  private static String STR;
  private static String SECRET;
  private static String SALT;
  private static String ENCRYPTED;

  private CryptoService cryptoService;

  @Mock
  private MessageError messageError;
  @Mock
  private ApiError apiError;

  @BeforeAll
  public static void beforeAll() {
    STR = "14291996799";
    SECRET = "secret";
    SALT = "salt";
    ENCRYPTED = "LmR25VLQwN1xAxz8oqifhQ==";
  }

  @BeforeEach
  public void beforeEach() {
    cryptoService = spy(new CryptServiceImpl(messageError, SECRET, SALT));
  }

  @DisplayName("Should encrypt aes successfully")
  @Test
  public void encryptAesSuccessfully() {
    assertEquals(ENCRYPTED, cryptoService.encryptAES256(STR));
  }

  @DisplayName("Should encrypt aes error")
  @Test
  public void encryptAesError() {
    when(messageError.create(Messages.AES_ENCRYPT_ERROR)).thenReturn(apiError);

    assertThrows(UnprocessableEntityException.class,
      () -> cryptoService.encryptAES256(null));
  }

  @DisplayName("Should decrypt aes successfully")
  @Test
  public void decryptAesSuccessfully() {
    assertEquals(STR, cryptoService.decryptAES256(ENCRYPTED));
  }

  @DisplayName("Should decrypt aes error")
  @Test
  public void decryptAesError() {
    when(messageError.create(Messages.AES_DECRYPT_ERROR)).thenReturn(apiError);

    assertThrows(UnprocessableEntityException.class,
      () -> cryptoService.decryptAES256(STR));
  }

}
