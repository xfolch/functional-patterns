package net.xfolch.patterns;

import java.util.concurrent.Callable;

/**
 * Represents some computation that is executed at request time
 * <p>
 * Created by xfolch on 13/5/16.
 */
public abstract class Lazy<T> extends AtMostOnce<T> implements Callable<T> {

    @Override
    public final T call() {
        try {
            return invoke();
        } catch (Exception e) {
            throw (RuntimeException) e;
        }
    }

    @Override
    protected abstract T get();

}
