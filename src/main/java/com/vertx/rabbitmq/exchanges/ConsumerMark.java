package com.vertx.rabbitmq.exchanges;

import java.util.concurrent.TimeoutException;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class ConsumerMark extends BasicConsumer {

    private ConsumerMark() {
        super(Constants.CONSUMER_MARK);
    }

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        BasicConsumer consumer = new ConsumerMark();
        consumer.start(onStart -> {
            consumer.declareQueue(onQueue -> {
                if (onQueue.succeeded()) {
                    consumer.bindQueueToExchange(Constants.EXCHANGE_NAME_USER, Constants.ROUTING_KEY_USER, onBind -> {
                        if (onBind.succeeded()) {
                            consumer.bindQueueToExchange(Constants.EXCHANGE_NAME_VEHICLE,
                                    Constants.ROUTING_KEY_VEHICLE,
                                    onSecondBind -> {
                                if (onSecondBind.succeeded()) {
                                    consumer.registerConsumer();
                                    consumer.basicConsume();
                                }
                            });
                        }
                    });
                }
            });
        });
    }

}
