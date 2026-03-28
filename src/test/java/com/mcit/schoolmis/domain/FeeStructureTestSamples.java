package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeeStructureTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeeStructure getFeeStructureSample1() {
        return new FeeStructure().id(1L).academicYear("academicYear1");
    }

    public static FeeStructure getFeeStructureSample2() {
        return new FeeStructure().id(2L).academicYear("academicYear2");
    }

    public static FeeStructure getFeeStructureRandomSampleGenerator() {
        return new FeeStructure().id(longCount.incrementAndGet()).academicYear(UUID.randomUUID().toString());
    }
}
