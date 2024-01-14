package com.example.inventoryservice.integration;

import com.example.inventoryservice.config.RabbitConfig;
import com.example.inventoryservice.events.payloads.OrderPayload;
import com.example.inventoryservice.services.OrderService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.stereotype.Service;

@Service
public class OrderEventFlow {

  @Autowired
  private ConnectionFactory connectionFactory;

  @Autowired
  private OrderService service;

  @Bean
  public IntegrationFlow messageInbound() {
    return IntegrationFlows
        .from(Amqp.inboundAdapter(connectionFactory, RabbitConfig.QUEUE_CREATED))
        .log(LoggingHandler.Level.INFO, new LiteralExpression("Starting Order Flow -> Process order created"))
        .handle( OrderPayload.class, (payload, header)-> service.processNewOrderInventory(payload))
        .handle(Long.class, ((payload, headers) -> service.confirmInventory(payload)))
        .log(LoggingHandler.Level.INFO, new LiteralExpression("Finished Order Flow"))
        .get();
  }

}
