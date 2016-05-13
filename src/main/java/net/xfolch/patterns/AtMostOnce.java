package net.xfolch.patterns;

/**
 * Represents a computation that is executed at most once
 * <p>
 * Created by xfolch on 13/5/16.
 */
public abstract class AtMostOnce<T> {

    private final Object lock = new Object();
    private volatile Either<Exception, T> result;

    protected final T invoke() throws Exception {
        if (result == null) {
            synchronized (lock) {
                if (result == null) {
                    result = execute();
                }
            }
        }

        if (result.isRight()) {
            return result.right();
        }

        throw result.left();
    }

    private Either<Exception, T> execute() {
        try {
            return Either.right(get());
        } catch (Exception e) {
            return Either.left(e);
        }
    }

    protected abstract T get() throws Exception;

}
