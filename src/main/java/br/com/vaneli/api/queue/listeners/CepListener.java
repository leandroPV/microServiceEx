package br.com.vaneli.api.queue.listeners;

import br.com.vaneli.api.queue.json.CepData;
import br.com.vaneli.api.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CepListener {

  private final UserService userService;

  public CepListener(UserService userService) {
    this.userService = userService;
  }

  @RabbitListener(queues = "${queues.cepQueue}")
  public void listen(CepData cepData) {
    log.info("Processing get cep by API. userId={} cep={}",
      cepData.getUserId(), cepData.getCep());

    this.userService.addAddressToUserByCep(cepData);

    log.info("processing get cep by API finished. userId={} cep={}",
      cepData.getUserId(), cepData.getCep());
  }

}
