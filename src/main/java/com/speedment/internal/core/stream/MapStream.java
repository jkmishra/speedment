package com.speedment.internal.core.stream;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * A java {@code Stream} wrapper that stream over Key-Value pairs. With this
 * wrapper you get access to additional operators for working with two valued
 * collections.
 * 
 * @author Emil
 * @param <K>  the key type
 * @param <V>  the value type
 */
public final class MapStream<K, V> implements Stream<Map.Entry<K, V>> {
    
    private final Stream<Map.Entry<K, V>> inner;
    
    public static <K, V> MapStream<K, V> of(Map.Entry<K, V> entry) {
        return new MapStream<>(Stream.of(entry));
    }
    
    public static <K, V> MapStream<K, V> of(Map.Entry<K, V>... entries) {
        return new MapStream<>(Stream.of(entries));
    }
    
    public static <K, V> MapStream<K, V> of(Map<K, V> map) {
        return new MapStream<>(map.entrySet().stream());
    }
    
    public static <K, V> MapStream<K, V> of(Stream<Map.Entry<K, V>> stream) {
        return new MapStream<>(stream);
    }
    
    public static <K, V> MapStream<K, V> empty() {
        return new MapStream<>(Stream.empty());
    }

    @Override
    public MapStream<K, V> filter(Predicate<? super Map.Entry<K, V>> predicate) {
        inner.filter(predicate);
        return this;
    }
    
    public MapStream<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        return filter(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @Override
    public <R> Stream<R> map(Function<? super Map.Entry<K, V>, ? extends R> mapper) {
        return inner.map(mapper);
    }
    
    public <R> Stream<R> map(BiFunction<? super K, ? super V, ? extends R> mapper) {
        return map(e -> mapper.apply(e.getKey(), e.getValue()));
    }
    
    public <R> MapStream<R, V> mapKey(BiFunction<? super K, ? super V, ? extends R> mapper) {
        return new MapStream<>(inner.map(e -> 
            new AbstractMap.SimpleEntry<>(
                mapper.apply(e.getKey(), e.getValue()),
                e.getValue()
            )
        ));
    }
    
    public <R> MapStream<K, R> mapValue(BiFunction<? super K, ? super V, ? extends R> mapper) {
        return new MapStream<>(inner.map(e -> 
            new AbstractMap.SimpleEntry<>(
                e.getKey(), 
                mapper.apply(e.getKey(), e.getValue())
            )
        ));
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super Map.Entry<K, V>> mapper) {
        return inner.mapToInt(mapper);
    }
    
    public IntStream mapToInt(ToIntBiFunction<? super K, ? super V> mapper) {
        return inner.mapToInt(e -> mapper.applyAsInt(e.getKey(), e.getValue()));
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super Map.Entry<K, V>> mapper) {
        return inner.mapToLong(mapper);
    }
    
    public LongStream mapToLong(ToLongBiFunction<? super K, ? super V> mapper) {
        return inner.mapToLong(e -> mapper.applyAsLong(e.getKey(), e.getValue()));
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super Map.Entry<K, V>> mapper) {
        return inner.mapToDouble(mapper);
    }
    
    public DoubleStream mapToDouble(ToDoubleBiFunction<? super K, ? super V> mapper) {
        return inner.mapToDouble(e -> mapper.applyAsDouble(e.getKey(), e.getValue()));
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super Map.Entry<K, V>, ? extends Stream<? extends R>> mapper) {
        return inner.flatMap(mapper);
    }
    
    public <R> Stream<R> flatMap(BiFunction<? super K, ? super V, ? extends Stream<? extends R>> mapper) {
        return inner.flatMap(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    public IntStream flatMapToInt(Function<? super Map.Entry<K, V>, ? extends IntStream> mapper) {
        return inner.flatMapToInt(mapper);
    }
    
    public IntStream flatMapToInt(BiFunction<? super K, ? super V, ? extends IntStream> mapper) {
        return inner.flatMapToInt(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    public LongStream flatMapToLong(Function<? super Map.Entry<K, V>, ? extends LongStream> mapper) {
        return inner.flatMapToLong(mapper);
    }
    
    public LongStream flatMapToLong(BiFunction<? super K, ? super V, ? extends LongStream> mapper) {
        return inner.flatMapToLong(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super Map.Entry<K, V>, ? extends DoubleStream> mapper) {
        return inner.flatMapToDouble(mapper);
    }
    
    public DoubleStream flatMapToDouble(BiFunction<? super K, ? super V, ? extends DoubleStream> mapper) {
        return inner.flatMapToDouble(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    @Override
    public MapStream<K, V> distinct() {
        inner.distinct();
        return this;
    }

    @Override
    public MapStream<K, V> sorted() {
        inner.sorted();
        return this;
    }

    @Override
    public MapStream<K, V> sorted(Comparator<? super Map.Entry<K, V>> comparator) {
        inner.sorted(comparator);
        return this;
    }

    public MapStream<K, V> sorted(ToIntBiFunction<? super K, ? super V> comparator) {
        inner.sorted((e1, e2) -> 
            comparator.applyAsInt(e1.getKey(), e1.getValue()) -
            comparator.applyAsInt(e2.getKey(), e2.getValue())
        );
        return this;
    }

    @Override
    public MapStream<K, V> peek(Consumer<? super Map.Entry<K, V>> action) {
        inner.peek(action);
        return this;
    }
    
    public MapStream<K, V> peek(BiConsumer<? super K, ? super V> action) {
        inner.peek(e -> action.accept(e.getKey(), e.getValue()));
        return this;
    }

    @Override
    public MapStream<K, V> limit(long maxSize) {
        inner.limit(maxSize);
        return this;
    }

    @Override
    public MapStream<K, V> skip(long n) {
        inner.skip(n);
        return this;
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<K, V>> action) {
        inner.forEach(action);
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action) {
        inner.forEach(e -> action.accept(e.getKey(), e.getValue()));
    }

    @Override
    public void forEachOrdered(Consumer<? super Map.Entry<K, V>> action) {
        inner.forEachOrdered(action);
    }
    
    public void forEachOrdered(BiConsumer<? super K, ? super V> action) {
        inner.forEachOrdered(e -> action.accept(e.getKey(), e.getValue()));
    }

    @Override
    public Object[] toArray() {
        return inner.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return inner.toArray(generator);
    }

    @Override
    public Map.Entry<K, V> reduce(Map.Entry<K, V> identity, BinaryOperator<Map.Entry<K, V>> accumulator) {
        return inner.reduce(identity, accumulator);
    }

    @Override
    public Optional<Map.Entry<K, V>> reduce(BinaryOperator<Map.Entry<K, V>> accumulator) {
        return inner.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super Map.Entry<K, V>, U> accumulator, BinaryOperator<U> combiner) {
        return inner.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Map.Entry<K, V>> accumulator, BiConsumer<R, R> combiner) {
        return inner.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super Map.Entry<K, V>, A, R> collector) {
        return inner.collect(collector);
    }

    @Override
    public Optional<Map.Entry<K, V>> min(Comparator<? super Map.Entry<K, V>> comparator) {
        return inner.min(comparator);
    }
    
    public Optional<Map.Entry<K, V>> min(ToIntBiFunction<? super K, ? super V> comparator) {
        return inner.min((e1, e2) -> 
            comparator.applyAsInt(e1.getKey(), e1.getValue()) -
            comparator.applyAsInt(e2.getKey(), e2.getValue())
        );
    }

    @Override
    public Optional<Map.Entry<K, V>> max(Comparator<? super Map.Entry<K, V>> comparator) {
        return inner.max(comparator);
    }
    
    public Optional<Map.Entry<K, V>> max(ToIntBiFunction<? super K, ? super V> comparator) {
        return inner.max((e1, e2) -> 
            comparator.applyAsInt(e1.getKey(), e1.getValue()) -
            comparator.applyAsInt(e2.getKey(), e2.getValue())
        );
    }

    @Override
    public long count() {
        return inner.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super Map.Entry<K, V>> predicate) {
        return inner.anyMatch(predicate);
    }
    
    public boolean anyMatch(BiPredicate<? super K, ? super V> predicate) {
        return inner.anyMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @Override
    public boolean allMatch(Predicate<? super Map.Entry<K, V>> predicate) {
        return inner.allMatch(predicate);
    }
    
    public boolean allMatch(BiPredicate<? super K, ? super V> predicate) {
        return inner.allMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @Override
    public boolean noneMatch(Predicate<? super Map.Entry<K, V>> predicate) {
        return inner.noneMatch(predicate);
    }
    
    public boolean noneMatch(BiPredicate<? super K, ? super V> predicate) {
        return inner.noneMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @Override
    public Optional<Map.Entry<K, V>> findFirst() {
        return inner.findFirst();
    }

    @Override
    public Optional<Map.Entry<K, V>> findAny() {
        return inner.findAny();
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return inner.iterator();
    }

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return inner.spliterator();
    }

    @Override
    public boolean isParallel() {
        return inner.isParallel();
    }

    @Override
    public MapStream<K, V> sequential() {
        inner.sequential();
        return this;
    }

    @Override
    public MapStream<K, V> parallel() {
        inner.parallel();
        return this;
    }

    @Override
    public MapStream<K, V> unordered() {
        inner.unordered();
        return this;
    }

    @Override
    public MapStream<K, V> onClose(Runnable closeHandler) {
        inner.onClose(closeHandler);
        return this;
    }

    @Override
    public void close() {
        inner.close();
    }
    
    private MapStream(Stream<Map.Entry<K, V>> inner) {
        this.inner = inner;
    }
}