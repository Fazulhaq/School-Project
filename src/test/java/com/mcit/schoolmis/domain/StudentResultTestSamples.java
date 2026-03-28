package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class StudentResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StudentResult getStudentResultSample1() {
        return new StudentResult().id(1L);
    }

    public static StudentResult getStudentResultSample2() {
        return new StudentResult().id(2L);
    }

    public static StudentResult getStudentResultRandomSampleGenerator() {
        return new StudentResult().id(longCount.incrementAndGet());
    }
}
