package com.test.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static com.test.routing.Severity.*;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static Severity[] severities = {INFO, ERROR, WARNING, DEBUG};

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            for (Severity s : severities) {
                //here s is routing key
                String message = "Message " + s.name();
                channel.basicPublish(EXCHANGE_NAME, String.valueOf(s), null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + s + "':'" + s.name() + "'");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
