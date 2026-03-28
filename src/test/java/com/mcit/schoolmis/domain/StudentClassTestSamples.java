package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudentClassTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudentClass getStudentClassSample1() {
        return new StudentClass().id(1L).name("name1");
    }

    public static StudentClass getStudentClassSample2() {
        return new StudentClass().id(2L).name("name2");
    }

    public static StudentClass getStudentClassRandomSampleGenerator() {
        return new StudentClass().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
