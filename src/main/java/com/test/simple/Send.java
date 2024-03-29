package com.test.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        // create connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < 100; i++) {
                String message = "Message:" + i;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
                Thread.sleep(100);
            }
        } catch (TimeoutException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
