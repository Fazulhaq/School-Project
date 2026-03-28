package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExamTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Exam getExamSample1() {
        return new Exam().id(1L).name("name1").academicYear("academicYear1");
    }

    public static Exam getExamSample2() {
        return new Exam().id(2L).name("name2").academicYear("academicYear2");
    }

    public static Exam getExamRandomSampleGenerator() {
        return new Exam().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).academicYear(UUID.randomUUID().toString());
    }
}
