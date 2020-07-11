package com.nikita488.zycraft.util;

import java.util.Objects;

@FunctionalInterface
public interface IntBiConsumer
{
    void accept(int t, int u);

    default IntBiConsumer andThen(IntBiConsumer after)
    {
        Objects.requireNonNull(after);
        return (int l, int r) -> { accept(l, r); after.accept(l, r); };
    }
}
