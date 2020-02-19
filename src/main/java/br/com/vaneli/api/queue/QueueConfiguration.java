package br.com.vaneli.api.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

  private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
  private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

  private final String exchange;
  private final String cepQueue;

  public QueueConfiguration(
    @Value("${spring.rabbitmq.template.exchange}") String exchange,
    @Value("${queues.cepQueue}") String cepQueue) {
    this.exchange = exchange;
    this.cepQueue = cepQueue;
  }

  @Bean("exchange")
  DirectExchange exchange() {
    return (DirectExchange) ExchangeBuilder.directExchange(exchange).build();
  }

  @Bean("cepQueue")
  public Queue cepQueue(
    @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange,
    @Qualifier("cepDeadLetterQueue") Queue deadLetterQueue) {
    return QueueBuilder.durable(this.cepQueue)
      .withArgument(X_DEAD_LETTER_EXCHANGE, deadLetterExchange.getName())
      .withArgument(X_DEAD_LETTER_ROUTING_KEY, deadLetterQueue.getName())
      .build();
  }

  @Bean
  Binding bindingCep(
    @Qualifier("cepQueue") Queue queue,
    @Qualifier("exchange") DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
