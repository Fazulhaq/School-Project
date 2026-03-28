package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.SectionTestSamples.*;
import static com.mcit.schoolmis.domain.SubjectTestSamples.*;
import static com.mcit.schoolmis.domain.TeacherTestSamples.*;
import static com.mcit.schoolmis.domain.TimetableTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimetableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timetable.class);
        Timetable timetable1 = getTimetableSample1();
        Timetable timetable2 = new Timetable();
        assertThat(timetable1).isNotEqualTo(timetable2);

        timetable2.setId(timetable1.getId());
        assertThat(timetable1).isEqualTo(timetable2);

        timetable2 = getTimetableSample2();
        assertThat(timetable1).isNotEqualTo(timetable2);
    }

    @Test
    void sectionTest() {
        Timetable timetable = getTimetableRandomSampleGenerator();
        Section sectionBack = getSectionRandomSampleGenerator();

        timetable.setSection(sectionBack);
        assertThat(timetable.getSection()).isEqualTo(sectionBack);

        timetable.section(null);
        assertThat(timetable.getSection()).isNull();
    }

    @Test
    void subjectTest() {
        Timetable timetable = getTimetableRandomSampleGenerator();
        Subject subjectBack = getSubjectRandomSampleGenerator();

        timetable.setSubject(subjectBack);
        assertThat(timetable.getSubject()).isEqualTo(subjectBack);

        timetable.subject(null);
        assertThat(timetable.getSubject()).isNull();
    }

    @Test
    void teacherTest() {
        Timetable timetable = getTimetableRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        timetable.setTeacher(teacherBack);
        assertThat(timetable.getTeacher()).isEqualTo(teacherBack);

        timetable.teacher(null);
        assertThat(timetable.getTeacher()).isNull();
    }
}
