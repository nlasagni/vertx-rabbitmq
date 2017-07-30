package com.wedriveu.rd;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;

import java.util.concurrent.TimeoutException;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class BasicConsumer {

    private static String QUEUE_NAME = "user-login";

    protected String tag;
    protected String queueName;
    protected Vertx vertx;
    protected RabbitMQClient client;
    protected String name;

    public BasicConsumer(String name) {
        this.name = name;
        this.tag = name;
        this.queueName = QUEUE_NAME + "-" + name;
        this.vertx = Vertx.vertx();
        JsonObject config = new JsonObject();
        config.put("host", Constants.HOST);
        client = RabbitMQClient.create(vertx, config);
    }

    public void start(Handler<AsyncResult<Void>> handler) throws java.io.IOException, TimeoutException {
        client.start(onStartCompleted -> {
            if (onStartCompleted.succeeded()) {
                Log.info(tag, "RabbitMQ client started");

            } else {
                Log.error(tag, onStartCompleted.cause().getMessage(), onStartCompleted.cause());
                handler.handle(io.vertx.core.Future.failedFuture(onStartCompleted.cause().getMessage()));
            }
        });
    }

    protected void startDefaultBehaviour(Handler<AsyncResult<Void>> handler) {
        declareQueue(onQueueDeclared -> {
            if (onQueueDeclared.succeeded()) {

            }
        });
    }

    protected void declareQueue(Handler<AsyncResult<Void>> handler) {
        // Here we could also use only one queue instead of creating a new queue for
        // every user
        client.queueDeclare(queueName, true, false, false, onDeclareCompleted -> {
            if (onDeclareCompleted.succeeded()) {
                String queueName = onDeclareCompleted.result().getString(Constants.QUEUE_NAME_JSON_KEY);
                Log.info(tag, "Declared queue " + queueName);
                handler.handle(Future.succeededFuture());
                bindQueueToExchange(Constants.EXCHANGE_NAME_USER, Constants.ROUTING_KEY_USER, handler);
            } else {
                Log.error(tag, onDeclareCompleted.cause().getMessage(), onDeclareCompleted.cause());
                handler.handle(Future.failedFuture(onDeclareCompleted.cause().getMessage()));
            }
        });
    }

    protected void bindQueueToExchange(String exchangeName, String baseRoutingKey, Handler<AsyncResult<Void>> handler) {
        client.queueBind(queueName,
                exchangeName,
                String.format(baseRoutingKey, name),
                onBind -> {
                    if (onBind.succeeded()) {
                        Log.info(tag,
                                "Bound " + queueName + " to exchange \"" + exchangeName + "\"");
                        registerConsumer(handler);
                    } else {
                        Log.error(tag, onBind.cause().getMessage(), onBind.cause());
                        handler.handle(io.vertx.core.Future.failedFuture(onBind.cause().getMessage()));
                    }
                });
    }

    protected void registerConsumer(Handler<AsyncResult<Void>> handler) {
        client.basicConsume(queueName, Constants.EVENT_BUS_ADDRESS, onRegistered -> {
            if (onRegistered.succeeded()) {
                Log.info(tag,"Registered to queue " + queueName);
                consume();
            } else {
                Log.error(tag, onRegistered.cause().getMessage(), onRegistered.cause());
                handler.handle(io.vertx.core.Future.failedFuture(onRegistered.cause().getMessage()));
            }
        });
    }

    protected void consume() {
        // Create the event bus handler which messages will be sent to
        vertx.eventBus().consumer(Constants.EVENT_BUS_ADDRESS, msg -> {
            JsonObject json = (JsonObject) msg.body();
            System.out.println("Got message: " + json.getString("body"));
        });
    }


}
