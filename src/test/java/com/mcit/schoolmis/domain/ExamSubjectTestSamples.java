package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExamSubjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ExamSubject getExamSubjectSample1() {
        return new ExamSubject().id(1L).maxMarks(1);
    }

    public static ExamSubject getExamSubjectSample2() {
        return new ExamSubject().id(2L).maxMarks(2);
    }

    public static ExamSubject getExamSubjectRandomSampleGenerator() {
        return new ExamSubject().id(longCount.incrementAndGet()).maxMarks(intCount.incrementAndGet());
    }
}
