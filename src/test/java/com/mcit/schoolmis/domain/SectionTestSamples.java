package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Section getSectionSample1() {
        return new Section().id(1L).name("name1");
    }

    public static Section getSectionSample2() {
        return new Section().id(2L).name("name2");
    }

    public static Section getSectionRandomSampleGenerator() {
        return new Section().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
