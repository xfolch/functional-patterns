package net.xfolch.patterns;

import com.google.common.base.Optional;

/**
 * Represents a computation that either can go well or can go wrong.
 * <p>
 * This abstraction gives us some flexibility to manage different cases.
 * <p>
 * Created by xfolch on 13/5/16.
 */
public abstract class Try<T> extends AtMostOnce<T> {

    public final T get(ErrorHandler<T, Exception> errorHandler) {
        try {
            return invoke();
        } catch (Exception e) {
            return errorHandler.handle(e);
        }
    }

    public final T getOrThrowException() throws Exception {
        return invoke();
    }

    public final T getOrThrowRuntimeException() {
        try {
            return invoke();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public final Optional<T> toOptional() {
        try {
            return Optional.fromNullable(invoke());
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    public final Either<Exception, T> toEither() {
        try {
            return Either.right(invoke());
        } catch (Exception e) {
            return Either.left(e);
        }
    }

    public final boolean isError() {
        return toEither().isLeft();
    }

}
