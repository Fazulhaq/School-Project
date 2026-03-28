package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.ExamSubjectTestSamples.*;
import static com.mcit.schoolmis.domain.StudentResultTestSamples.*;
import static com.mcit.schoolmis.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentResult.class);
        StudentResult studentResult1 = getStudentResultSample1();
        StudentResult studentResult2 = new StudentResult();
        assertThat(studentResult1).isNotEqualTo(studentResult2);

        studentResult2.setId(studentResult1.getId());
        assertThat(studentResult1).isEqualTo(studentResult2);

        studentResult2 = getStudentResultSample2();
        assertThat(studentResult1).isNotEqualTo(studentResult2);
    }

    @Test
    void studentTest() {
        StudentResult studentResult = getStudentResultRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        studentResult.setStudent(studentBack);
        assertThat(studentResult.getStudent()).isEqualTo(studentBack);

        studentResult.student(null);
        assertThat(studentResult.getStudent()).isNull();
    }

    @Test
    void examSubjectTest() {
        StudentResult studentResult = getStudentResultRandomSampleGenerator();
        ExamSubject examSubjectBack = getExamSubjectRandomSampleGenerator();

        studentResult.setExamSubject(examSubjectBack);
        assertThat(studentResult.getExamSubject()).isEqualTo(examSubjectBack);

        studentResult.examSubject(null);
        assertThat(studentResult.getExamSubject()).isNull();
    }
}
