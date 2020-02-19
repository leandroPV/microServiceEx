package br.com.vaneli.api.queue.senders;

import br.com.vaneli.api.queue.json.CepData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CepSender {

  private final RabbitTemplate rabbitTemplate;

  private final String cepQueue;

  public CepSender(
    RabbitTemplate rabbitTemplate,
    @Value("${queues.cepQueue}") String cepQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.cepQueue = cepQueue;
  }

  public void addToQueue(CepData cepData) {
    this.rabbitTemplate.convertAndSend(this.cepQueue, cepData);
    log.info("Cep added to queue. userId={} cep={}", cepData.getUserId(), cepData.getCep());
  }
}
