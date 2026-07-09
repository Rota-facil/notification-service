package com.rota.facil.notification_service.messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
  @Value("${rabbitmq.transport.exchange}")
  private String transportExchange;

  @Value("${rabbitmq.auth.exchange}")
  private String authExchange;

  @Value("${rabbitmq.trip.cancelled.routing.key}")
  private String tripCancelledRoutingKey;

  @Value("${rabbitmq.trip.running.routing.key}")
  private String tripRunningRoutingKey;

  @Value("${rabbitmq.user.feedback.routing.key}")
  private String userFeedbackRoutingKey;

  @Value("${rabbitmq.user.created.routing.key}")
  private String userCreatedRoutingKey;

  @Value("${rabbitmq.user.deleted.routing.key}")
  private String userDeletedRoutingKey;

  @Value("${rabbitmq.notification.trip.cancelled.queue}")
  private String tripCancelledQueue;

  @Value("${rabbitmq.notification.trip.running.queue}")
  private String tripRunningQueue;

  @Value("${rabbitmq.notification.user.feedback.queue}")
  private String userFeedbackQueue;

  @Value("${rabbitmq.notification.user.created.queue}")
  private String userCreatedQueue;

  @Value("${rabbitmq.notification.user.deleted.queue}")
  private String userDeletedQueue;

  @Bean
  public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public RabbitTemplate rabbitListener(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter);
    return template;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
    ConnectionFactory connectionFactory,
    Jackson2JsonMessageConverter messageConverter
  ) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter);
    return factory;
  }

  @Bean
  public TopicExchange transportExchange() {
    return new TopicExchange(transportExchange);
  }

  @Bean
  public TopicExchange authExchange() {
    return new TopicExchange(authExchange);
  }

  @Bean
  public Binding tripCancelledBinding() {
    return BindingBuilder.bind(this.tripCancelledQueue()).to(this.transportExchange()).with(tripCancelledRoutingKey);
  }

  @Bean
  public Binding tripRunningBinding() {
    return BindingBuilder.bind(this.tripRunningQueue()).to(this.transportExchange()).with(tripRunningRoutingKey);
  }

  @Bean
  public Binding userFeedbackBinding() {
    return BindingBuilder.bind(this.userFeedbackQueue()).to(this.transportExchange()).with(userFeedbackRoutingKey);
  }

  @Bean
  public Binding userCreatedBinding() {
    return BindingBuilder.bind(this.userCreatedQueue()).to(this.authExchange()).with(this.userCreatedRoutingKey);
  }

  @Bean
  public Binding userDeletedBinding() {
    return BindingBuilder.bind(this.userDeletedQueue()).to(this.authExchange()).with(this.userDeletedRoutingKey);
  }

  @Bean
  public Queue tripCancelledQueue() {
    return new Queue(tripCancelledQueue);
  }

  @Bean
  public Queue tripRunningQueue() {
    return new Queue(tripRunningQueue);
  }

  @Bean
  public Queue userFeedbackQueue() {
    return new Queue(userFeedbackQueue);
  }

  @Bean
  public Queue userCreatedQueue() {
    return new Queue(userCreatedQueue);
  }

  @Bean
  public Queue userDeletedQueue() {
    return new Queue(userDeletedQueue);
  }
}
