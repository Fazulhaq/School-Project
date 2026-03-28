package com.mcit.schoolmis.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TeacherSubjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TeacherSubject getTeacherSubjectSample1() {
        return new TeacherSubject().id(1L);
    }

    public static TeacherSubject getTeacherSubjectSample2() {
        return new TeacherSubject().id(2L);
    }

    public static TeacherSubject getTeacherSubjectRandomSampleGenerator() {
        return new TeacherSubject().id(longCount.incrementAndGet());
    }
}
