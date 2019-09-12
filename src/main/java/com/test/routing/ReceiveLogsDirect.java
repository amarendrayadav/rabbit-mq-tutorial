package com.test.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.test.routing.Severity.*;

public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static Severity[] severities = {INFO, ERROR, WARNING, DEBUG};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Channel channel = factory.newConnection().createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        for (Severity s : severities) {
            channel.queueBind(queueName, EXCHANGE_NAME, s.name()); //here queue is binded with channel by routing keys
        }
        DeliverCallback callback = (tag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(tag + " [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, callback, consumerTag -> {
        });
    }
}
