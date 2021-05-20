package com.juone.audizer.connectors;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class WitAIKeysPool {
    private final BlockingQueue<String> queue;
    public WitAIKeysPool() throws InterruptedException {
        queue = new ArrayBlockingQueue<>(100, true);
        //keys should be here
    }

    @Nullable
    public String getKey() {
        try {
            return queue.poll(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void releaseKey(@Nonnull String key) {
        try {
            queue.put(key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
