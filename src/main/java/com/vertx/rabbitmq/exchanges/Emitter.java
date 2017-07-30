package com.vertx.rabbitmq.exchanges;

import com.vertx.rabbitmq.util.Log;
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
                   declareExchanges(onDeclareCompleted -> {
                       if (onDeclareCompleted.succeeded()) {
                           Log.info(TAG, "Declared exchange \""
                                   + Constants.EXCHANGE_NAME_USER
                                   + "\" and \""
                                   + Constants.EXCHANGE_NAME_VEHICLE + "\"");
                           String message = "Hello RabbitMQ, from Vert.x !";
                           publishToConsumer(Constants.EXCHANGE_NAME_USER, String.format(Constants.ROUTING_KEY_USER, Constants.CONSUMER_MARK), message + " USER");
                           publishToConsumer(Constants.EXCHANGE_NAME_VEHICLE, String.format(Constants.ROUTING_KEY_VEHICLE, Constants.CONSUMER_MARK), message + " VEHICLE");
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

    private static void declareExchanges(Handler<AsyncResult<Void>> handler) {
        client.exchangeDeclare(Constants.EXCHANGE_NAME_USER,
                Constants.EXCHANGE_TYPE,
                false,
                false,
                onDeclare -> {
                    client.exchangeDeclare(Constants.EXCHANGE_NAME_VEHICLE,
                            Constants.EXCHANGE_TYPE,
                            false,
                            false,
                            handler);
        });
    }

    private static void publishToConsumer(String exchangeName,
                                          String routingKey,
                                          String message) {
        JsonObject messageObj = new JsonObject().put(Constants.MESSAGE_BODY, message);
        client.basicPublish(exchangeName, routingKey, messageObj, onPublish -> {
            if (onPublish.succeeded()) {
                Log.info(TAG, "Sent message to " + Constants.CONSUMER_MARK);
            } else {
                Log.error(TAG, onPublish.cause().getMessage(), onPublish.cause());
            }
        });
    }

}
