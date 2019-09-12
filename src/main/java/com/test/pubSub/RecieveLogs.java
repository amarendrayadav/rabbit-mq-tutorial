package com.test.pubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class RecieveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        DeliverCallback callback = (tag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(tag + " [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, callback, consumerTag -> {
        });

        //declaring another queue to and consume from that
        String queueName2 = channel.queueDeclare().getQueue();
        channel.queueBind(queueName2, EXCHANGE_NAME, "");

        channel.basicConsume(queueName2, true, callback, consumerTag -> {
        });

    }
}
