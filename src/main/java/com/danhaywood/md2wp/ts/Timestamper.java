package com.danhaywood.md2wp.ts;

import java.util.concurrent.atomic.AtomicLong;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class Timestamper {

    private AtomicLong lastTimestamp = new AtomicLong(java.time.Instant.now().toEpochMilli() / 50L * 50L); // round down to nearest 50 millis

    public @NotNull String timestamp() {
        lastTimestamp.updateAndGet(t -> t + 50L); // increment by 50 millis.
        return String.valueOf(lastTimestamp);
    }
}
