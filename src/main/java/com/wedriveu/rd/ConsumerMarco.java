package com.wedriveu.rd;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class ConsumerMarco extends BasicConsumer {

    private ConsumerMarco() {
        super(Constants.CONSUMER_MARCO);
    }

    @Override
    public void start() throws IOException, TimeoutException {
        super.start();
        bindQueueToExchange(Constants.EXCHANGE_NAME_VEHICLE, Constants.ROUTING_KEY_VEHICLE);
    }

    @Override
    protected void bindQueueToExchange(String exchangeName, String baseRoutingKey) {
        super.bindQueueToExchange(exchangeName, baseRoutingKey);
        client.queueBind(queueName,
                exchangeName,
                String.format(baseRoutingKey, name),
                onBind -> {
                    if (onBind.succeeded()) {
                        Log.info(tag,
                                "Bound " + queueName + " to exchange \"" + exchangeName + "\"");
                        registerConsumer();
                    } else {
                        Log.error(tag, onBind.cause().getMessage(), onBind.cause());
                    }
                });
    }

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        new ConsumerMarco().start();
    }

}
