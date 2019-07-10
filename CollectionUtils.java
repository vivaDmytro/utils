package org.dmytro;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class CollectionUtils {

	public static <T> Collection<List<T>> splitByChunks(List<T> numbers, Integer chunkSize) {
		final AtomicInteger counter = new AtomicInteger();

		return numbers.stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
				.values();
	}
}