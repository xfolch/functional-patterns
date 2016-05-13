package net.xfolch.patterns;

/**
 * The purpose of this abstraction is to separate execution from error management
 *
 * Created by xfolch on 13/5/16.
 */
public interface ErrorHandler<T, E extends Exception> {

    /**
     * Manages the given exception returning a fallback value or throwing a {@link RuntimeException}
     *
     * @param e the exception that has to be managed
     * @return a fallback value
     * @throws RuntimeException when there is no fallback at all
     */
    T handle(E e);

}
