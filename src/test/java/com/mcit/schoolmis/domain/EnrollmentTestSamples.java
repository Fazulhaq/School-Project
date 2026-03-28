package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EnrollmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Enrollment getEnrollmentSample1() {
        return new Enrollment().id(1L).academicYear("academicYear1").rollNumber(1);
    }

    public static Enrollment getEnrollmentSample2() {
        return new Enrollment().id(2L).academicYear("academicYear2").rollNumber(2);
    }

    public static Enrollment getEnrollmentRandomSampleGenerator() {
        return new Enrollment()
            .id(longCount.incrementAndGet())
            .academicYear(UUID.randomUUID().toString())
            .rollNumber(intCount.incrementAndGet());
    }
}
