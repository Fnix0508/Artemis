package com.fnix.artemis.core.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Stream2 {

    public static  <T, R> List<R> map(Collection<T> collection, Function<? super T, R> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T> void forEach(T[] arrays, Consumer<? super T> action) {
        Arrays.asList(arrays).forEach(action);
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper,
                            Function<? super T, ? extends V> valueMapper) {
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<? super T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper) {
        return collection.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <T, R> List<R> map(T[] arrays, Function<? super T, R> mapper) {
        return map(Arrays.asList(arrays), mapper);
    }

    public static int sum(int[] arrays) {
        return Arrays.stream(arrays).sum();
    }

    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        return collection.stream().sorted(comparator).collect(Collectors.toList());
    }

}
