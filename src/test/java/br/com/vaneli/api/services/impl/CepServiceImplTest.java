package br.com.vaneli.api.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import br.com.vaneli.api.interfaces.json.Address;
import br.com.vaneli.api.services.CepService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class CepServiceImplTest {

  private static String CEP;
  private static String URI_CEP;
  private static String URI_TO_STRING;

  private CepService cepService;

  private HttpEntity<?> httpEntity;

  @Mock
  private RestTemplate restTemplate;
  @Mock
  private ResponseEntity<Address> responseEntityAddress;
  @Mock
  private Address address;
  @Mock
  private HttpClientErrorException httpClientErrorException;
  @Mock
  private HttpServerErrorException httpServerErrorException;

  @BeforeAll
  public static void beforeAll() {
    URI_CEP = "http://localhost:8090";
    CEP = "123456";
    URI_TO_STRING = URI_CEP + "/" + "cep/" + CEP;
  }

  @BeforeEach
  public void beforeEach() {
    cepService = spy(new CepServiceImpl(restTemplate, URI_CEP));
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);
    httpEntity = new HttpEntity<>(header);
  }

  @DisplayName("Should get cep api successfully")
  @Test
  public void getCepApiSuccessfully() {
    when(restTemplate.exchange(URI_TO_STRING, HttpMethod.GET, httpEntity, Address.class))
      .thenReturn(responseEntityAddress);
    when(responseEntityAddress.getBody()).thenReturn(address);
    when(responseEntityAddress.getStatusCodeValue()).thenReturn(200);

    assertEquals(address, cepService.getCep(CEP));
  }

  @DisplayName("Should get cep api client error")
  @Test
  public void getCepApiClientError() {
    when(restTemplate.exchange(URI_TO_STRING, HttpMethod.GET, httpEntity, Address.class))
      .thenThrow(httpClientErrorException);

    assertNull(cepService.getCep(CEP));
  }

  @DisplayName("Should get cep api server error")
  @Test
  public void getCepApiServerError() {
    when(restTemplate.exchange(URI_TO_STRING, HttpMethod.GET, httpEntity, Address.class))
      .thenThrow(httpServerErrorException);

    assertNull(cepService.getCep(CEP));
  }

}
