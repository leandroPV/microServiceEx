package br.com.vaneli.api.services.impl;

import br.com.vaneli.api.interfaces.json.Address;
import br.com.vaneli.api.services.CepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class CepServiceImpl implements CepService {

  private final String CEP_PATH = "cep";

  private final RestTemplate restTemplate;
  private final String uriCep;

  public CepServiceImpl(
    RestTemplate restTemplate,
    @Value("${rest.api.cep}") String uriCep) {
    this.restTemplate = restTemplate;
    this.uriCep = uriCep;
  }

  @Override
  public Address getCep(String cep) {

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uriCep)
      .pathSegment(CEP_PATH)
      .pathSegment(cep)
      .encode();

    try {
      log.info(
        "Requesting to {}",
        uriBuilder.toUriString()
      );

      ResponseEntity<Address> response = restTemplate.exchange(
        uriBuilder.toUriString(),
        HttpMethod.GET,
        new HttpEntity<>(buildClientIdHeader()),
        Address.class
      );

      log.info("Request received! status={}", response.getStatusCodeValue());
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      log.warn("Erro when integration CEP API. Response: {}", e.getStatusCode());
    }

    return null;

  }

  protected HttpHeaders buildClientIdHeader() {
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);
    return header;
  }

}
