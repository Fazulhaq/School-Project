package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.ExamSubjectTestSamples.*;
import static com.mcit.schoolmis.domain.ExamTestSamples.*;
import static com.mcit.schoolmis.domain.SubjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamSubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamSubject.class);
        ExamSubject examSubject1 = getExamSubjectSample1();
        ExamSubject examSubject2 = new ExamSubject();
        assertThat(examSubject1).isNotEqualTo(examSubject2);

        examSubject2.setId(examSubject1.getId());
        assertThat(examSubject1).isEqualTo(examSubject2);

        examSubject2 = getExamSubjectSample2();
        assertThat(examSubject1).isNotEqualTo(examSubject2);
    }

    @Test
    void examTest() {
        ExamSubject examSubject = getExamSubjectRandomSampleGenerator();
        Exam examBack = getExamRandomSampleGenerator();

        examSubject.setExam(examBack);
        assertThat(examSubject.getExam()).isEqualTo(examBack);

        examSubject.exam(null);
        assertThat(examSubject.getExam()).isNull();
    }

    @Test
    void subjectTest() {
        ExamSubject examSubject = getExamSubjectRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        examSubject.setSubject(subjectBack);
        assertThat(examSubject.getSubject()).isEqualTo(subjectBack);

        examSubject.subject(null);
        assertThat(examSubject.getSubject()).isNull();
    }
}
