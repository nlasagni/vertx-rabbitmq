package com.vertx.rabbitmq.exchanges;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public interface Constants {

    String HOST = "localhost";
    String EXCHANGE_NAME_USER = "user";
    String EXCHANGE_NAME_VEHICLE = "vehicle";
    String EXCHANGE_TYPE = "direct";
    String ROUTING_KEY_USER = "user.login.%s";
    String ROUTING_KEY_VEHICLE = "vehicle.nearest.%s";
    String MESSAGE_BODY = "body";
    String QUEUE_NAME_JSON_KEY = "queue";

    String EVENT_BUS_ADDRESS = "user.login";

    String CONSUMER_MARK = "mark";
    String CONSUMER_MICHAEL = "michael";

}
