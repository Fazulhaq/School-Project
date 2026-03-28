package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.EnrollmentTestSamples.*;
import static com.mcit.schoolmis.domain.SectionTestSamples.*;
import static com.mcit.schoolmis.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enrollment.class);
        Enrollment enrollment1 = getEnrollmentSample1();
        Enrollment enrollment2 = new Enrollment();
        assertThat(enrollment1).isNotEqualTo(enrollment2);

        enrollment2.setId(enrollment1.getId());
        assertThat(enrollment1).isEqualTo(enrollment2);

        enrollment2 = getEnrollmentSample2();
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }

    @Test
    void studentTest() {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        enrollment.setStudent(studentBack);
        assertThat(enrollment.getStudent()).isEqualTo(studentBack);

        enrollment.student(null);
        assertThat(enrollment.getStudent()).isNull();
    }

    @Test
    void sectionTest() {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        Section sectionBack = getSectionRandomSampleGenerator();

        enrollment.setSection(sectionBack);
        assertThat(enrollment.getSection()).isEqualTo(sectionBack);

        enrollment.section(null);
        assertThat(enrollment.getSection()).isNull();
    }
}
