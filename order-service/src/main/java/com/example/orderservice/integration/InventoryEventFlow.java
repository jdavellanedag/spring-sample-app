package com.example.orderservice.integration;

import com.example.orderservice.config.RabbitConfig;
import com.example.orderservice.events.payloads.ConfirmStockPayload;
import com.example.orderservice.service.OrderService;
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
public class InventoryEventFlow {

  @Autowired
  private ConnectionFactory connectionFactory;

  @Autowired
  private OrderService service;

  @Bean
  public IntegrationFlow messageInbound() {
    return IntegrationFlows
        .from(Amqp.inboundAdapter(connectionFactory, RabbitConfig.QUEUE_PLACED))
        .log(LoggingHandler.Level.INFO, new LiteralExpression("Starting Inventory Flow -> Updating confirmed order"))
        .handle( ConfirmStockPayload.class, (payload, header) -> service.updateOrderStatus(payload))
        .log(LoggingHandler.Level.INFO, new LiteralExpression("Finished Inventory Flow"))
        .get();
  }

}
