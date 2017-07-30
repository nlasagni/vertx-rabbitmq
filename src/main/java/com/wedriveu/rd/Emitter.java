package com.wedriveu.rd;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;

import java.util.concurrent.TimeoutException;
/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class Emitter {

    private static String TAG = Emitter.class.getSimpleName();
    private static RabbitMQClient client;

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        Vertx vertx = Vertx.vertx();
        JsonObject config = new JsonObject();
        config.put("host", Constants.HOST);
        client = RabbitMQClient.create(vertx, config);
        client.start(onStartCompleted -> {
                if (onStartCompleted.succeeded()) {
                    Log.info(TAG, "Started RabbitQM client");
                   declareExchange(onDeclareCompleted -> {
                       if (onDeclareCompleted.succeeded()) {
                           Log.info(TAG, "Declared exchange \"" + Constants.EXCHANGE_NAME_USER + "\"");
                           String message = "Hello RabbitMQ, from Vert.x !";
                           publishToConsumer(Constants.EXCHANGE_NAME_USER, Constants.CONSUMER_MARCO, message);
                       } else {
                           Log.error(TAG, onDeclareCompleted.cause().getMessage(), onDeclareCompleted.cause());
                       }
                   });
                } else {
                    Log.error(TAG, onStartCompleted.cause().getMessage(), onStartCompleted.cause());
                }
            }
        );
    }

    private static void declareExchange(Handler<AsyncResult<Void>> handler) {
        client.exchangeDeclare(Constants.EXCHANGE_NAME_USER, Constants.EXCHANGE_TYPE, false, false, handler);
    }

    private static void publishToConsumer(String exchangeName, String consumerName, String message) {
        String routingKey = String.format(Constants.ROUTING_KEY_USER, consumerName);
        JsonObject messageObj = new JsonObject().put(Constants.MESSAGE_BODY, message);
        client.basicPublish(exchangeName, routingKey, messageObj, onPublish -> {
            if (onPublish.succeeded()) {
                Log.info(TAG, "Sent message to " + Constants.CONSUMER_MARCO);
            } else {
                Log.error(TAG, onPublish.cause().getMessage(), onPublish.cause());
            }
        });
    }

}
