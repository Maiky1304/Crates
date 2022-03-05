package com.github.maiky1304.crates.utils.random;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@UtilityClass
public class RandomUtil {

    /**
     * Pick a random item from the list
     * @param list
     * @param <T>
     * @return random item from list
     */
    public static <T> T pickRandom(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    /**
     * Picks a random item using weight
     * @param list
     * @param extractor
     * @param <T>
     * @return random item from list
     */
    public static <T> T pickRandomWithChance(List<T> list, Function<T, Double> extractor) {
        double sum = list.stream().map(extractor).reduce(0d, (acc, cur) -> acc += cur);
        int random = ThreadLocalRandom.current().nextInt(0, (int) sum);


        for (T item : list) {
            double weight = extractor.apply(item);
            random -= (int) weight;
            if (random <= weight) {
                return item;
            }
        }

        return null;
    }

}
