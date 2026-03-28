package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.SectionTestSamples.*;
import static com.mcit.schoolmis.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Section.class);
        Section section1 = getSectionSample1();
        Section section2 = new Section();
        assertThat(section1).isNotEqualTo(section2);

        section2.setId(section1.getId());
        assertThat(section1).isEqualTo(section2);

        section2 = getSectionSample2();
        assertThat(section1).isNotEqualTo(section2);
    }

    @Test
    void studentClassTest() {
        Section section = getSectionRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        section.setStudentClass(studentClassBack);
        assertThat(section.getStudentClass()).isEqualTo(studentClassBack);

        section.studentClass(null);
        assertThat(section.getStudentClass()).isNull();
    }
}
