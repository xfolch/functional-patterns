package net.xfolch.patterns;

import com.google.common.base.Optional;

/**
 * Represents a value of one of two possible types (a disjoint union).
 * <p>
 * Very useful as return type of weak functions, in which the convention dictates that Left is used for failure and Right is used for success.
 * <p>
 * Created by xfolch on 13/5/16.
 */
public abstract class Either<L, R> {

    private final L left;
    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    public final L left() {
        return left;
    }

    public final R right() {
        return right;
    }

    public abstract boolean isRight();

    public abstract boolean isLeft();

    public final Optional<R> toOptional() {
        return Optional.fromNullable(right);
    }

    private static final class Left<L, R> extends Either<L, R> {

        private Left(L left) {
            super(left, null);
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

    }

    private static final class Right<L, R> extends Either<L, R> {

        private Right(R right) {
            super(null, right);
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

    }

}
