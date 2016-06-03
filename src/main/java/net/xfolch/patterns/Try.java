package net.xfolch.patterns;

import com.google.common.base.Optional;

import java.util.concurrent.Callable;

/**
 * Represents a computation that either can go well or can go wrong.
 * <p>
 * This abstraction gives us some flexibility to manage different failure scenarios.
 * <p>
 * Created by xfolch on 13/5/16.
 */
public abstract class Try<T> {

    private Try() {
    }

    public static <T> Try<T> newOne(Callable<T> callable) {
        try {
            return new Success<>(callable.call());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    public abstract T get(ErrorHandler<T, Exception> errorHandler);

    public abstract T getOrThrowRuntimeException();

    public abstract T getOrThrowException() throws Exception;

    public abstract Optional<T> toOptional();

    public abstract Either<Exception, T> toEither();

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

    public abstract <R> Try<R> map(Function<T, R> function);

    public abstract <R> Try<R> flatMap(Function<T, Try<R>> function);

    private static final class Success<T> extends Try<T> {
        private final T result;

        private Success(T result) {
            this.result = result;
        }

        @Override
        public T get(ErrorHandler<T, Exception> errorHandler) {
            return result;
        }

        @Override
        public T getOrThrowRuntimeException() {
            return result;
        }

        @Override
        public T getOrThrowException() {
            return result;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.fromNullable(result);
        }

        @Override
        public Either<Exception, T> toEither() {
            return Either.right(result);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public <R> Try<R> map(Function<T, R> function) {
            return map(result, function);
        }

        @Override
        public <R> Try<R> flatMap(Function<T, Try<R>> function) {
            return flat(map(result, function));
        }

        private static <S, D> Try<D> map(final S source, final Function<S, D> function) {
            return Try.newOne(new Callable<D>() {
                @Override
                public D call() throws Exception {
                    return function.apply(source);
                }
            });
        }

        private static <D> Try<D> flat(final Try<Try<D>> try1) {
            return Try.newOne(new Callable<D>() {
                @Override
                public D call() throws Exception {
                    return try1.getOrThrowException().getOrThrowException();
                }
            });
        }
    }

    private static final class Failure<T> extends Try<T> {
        private final Exception error;

        private Failure(Exception error) {
            this.error = error;
        }

        @Override
        public T get(ErrorHandler<T, Exception> errorHandler) {
            return errorHandler.handle(error);
        }

        @Override
        public T getOrThrowRuntimeException() {
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }

            throw new RuntimeException(error.getMessage(), error);
        }

        @Override
        public T getOrThrowException() throws Exception {
            throw error;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.absent();
        }

        @Override
        public Either<Exception, T> toEither() {
            return Either.left(error);
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Try<R> map(Function<T, R> function) {
            return (Try<R>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Try<R> flatMap(Function<T, Try<R>> function) {
            return (Try<R>) this;
        }
    }

    /**
     * Unlike {@link com.google.common.base.Function}, this abstraction could throw an exception
     *
     * @param <S> type of source
     * @param <D> type of destination
     */
    public interface Function<S, D> {
        D apply(S source) throws Exception;

        final class Factory {
            private Factory() {
            }

            /**
             * Converts a {@link com.google.common.base.Function}
             *
             * @param function
             * @param <S>
             * @param <D>
             * @return
             */
            public static <S, D> Function<S, D> from(final com.google.common.base.Function<S, D> function) {
                return new Function<S, D>() {
                    @Override
                    public D apply(S source) {
                        return function.apply(source);
                    }
                };
            }
        }
    }

}
