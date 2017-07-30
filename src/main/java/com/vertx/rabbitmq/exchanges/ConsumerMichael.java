package com.vertx.rabbitmq.exchanges;

import java.util.concurrent.TimeoutException;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class ConsumerMichael extends BasicConsumer {

    private ConsumerMichael() {
        super(Constants.CONSUMER_MICHAEL);
    }

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        BasicConsumer consumer = new ConsumerMichael();
        consumer.start(onStart -> {
            consumer.declareQueue(onQueue -> {
                if (onQueue.succeeded()) {
                    consumer.bindQueueToExchange(Constants.EXCHANGE_NAME_USER, Constants.ROUTING_KEY_USER, onBind -> {
                        if (onBind.succeeded()) {
                                            consumer.registerConsumer();
                                            consumer.basicConsume();
                        }
                    });
                }
            });
        });
    }

}
