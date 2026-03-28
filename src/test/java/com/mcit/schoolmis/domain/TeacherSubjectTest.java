package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.SectionTestSamples.*;
import static com.mcit.schoolmis.domain.SubjectTestSamples.*;
import static com.mcit.schoolmis.domain.TeacherSubjectTestSamples.*;
import static com.mcit.schoolmis.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeacherSubjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeacherSubject.class);
        TeacherSubject teacherSubject1 = getTeacherSubjectSample1();
        TeacherSubject teacherSubject2 = new TeacherSubject();
        assertThat(teacherSubject1).isNotEqualTo(teacherSubject2);

        teacherSubject2.setId(teacherSubject1.getId());
        assertThat(teacherSubject1).isEqualTo(teacherSubject2);

        teacherSubject2 = getTeacherSubjectSample2();
        assertThat(teacherSubject1).isNotEqualTo(teacherSubject2);
    }

    @Test
    void teacherTest() {
        TeacherSubject teacherSubject = getTeacherSubjectRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        teacherSubject.setTeacher(teacherBack);
        assertThat(teacherSubject.getTeacher()).isEqualTo(teacherBack);

        teacherSubject.teacher(null);
        assertThat(teacherSubject.getTeacher()).isNull();
    }

    @Test
    void subjectTest() {
        TeacherSubject teacherSubject = getTeacherSubjectRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        teacherSubject.setSubject(subjectBack);
        assertThat(teacherSubject.getSubject()).isEqualTo(subjectBack);

        teacherSubject.subject(null);
        assertThat(teacherSubject.getSubject()).isNull();
    }

    @Test
    void sectionTest() {
        TeacherSubject teacherSubject = getTeacherSubjectRandomSampleGenerator();
        Section sectionBack = getSectionRandomSampleGenerator();

        teacherSubject.setSection(sectionBack);
        assertThat(teacherSubject.getSection()).isEqualTo(sectionBack);

        teacherSubject.section(null);
        assertThat(teacherSubject.getSection()).isNull();
    }
}
