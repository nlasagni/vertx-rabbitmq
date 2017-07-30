package com.wedriveu.rd;

import java.util.concurrent.TimeoutException;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class ConsumerMichele extends BasicConsumer {

    private ConsumerMichele() {
        super(Constants.CONSUMER_MICHELE);
    }

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        new ConsumerMichele().start(onStart -> {});
    }

}
