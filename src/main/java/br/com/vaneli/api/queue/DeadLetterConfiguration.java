package br.com.vaneli.api.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterConfiguration {

  private final String exchange;
  private final String cepQueue;

  public DeadLetterConfiguration(
    @Value("${spring.rabbitmq.template.exchange}") String exchange,
    @Value("${queues.cepQueue}") String cepQueue) {
    String deadLetter = "-dl";
    this.exchange = exchange + deadLetter;
    this.cepQueue = cepQueue + deadLetter;
  }

  @Bean("deadLetterExchange")
  DirectExchange exchange() {
    return (DirectExchange) ExchangeBuilder.directExchange(exchange).build();
  }

  @Bean("cepDeadLetterQueue")
  public Queue cepDeadLetterQueue() {
    return QueueBuilder.durable(this.cepQueue).build();
  }

  @Bean
  Binding bindingCepDeadLetterQueue(
    @Qualifier("cepDeadLetterQueue") Queue queue,
    @Qualifier("deadLetterExchange") DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
  }

}
