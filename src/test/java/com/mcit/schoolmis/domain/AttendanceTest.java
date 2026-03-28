package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.AttendanceTestSamples.*;
import static com.mcit.schoolmis.domain.StudentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attendance.class);
        Attendance attendance1 = getAttendanceSample1();
        Attendance attendance2 = new Attendance();
        assertThat(attendance1).isNotEqualTo(attendance2);

        attendance2.setId(attendance1.getId());
        assertThat(attendance1).isEqualTo(attendance2);

        attendance2 = getAttendanceSample2();
        assertThat(attendance1).isNotEqualTo(attendance2);
    }

    @Test
    void studentTest() {
        Attendance attendance = getAttendanceRandomSampleGenerator();
        Student studentBack = getStudentRandomSampleGenerator();

        attendance.setStudent(studentBack);
        assertThat(attendance.getStudent()).isEqualTo(studentBack);

        attendance.student(null);
        assertThat(attendance.getStudent()).isNull();
    }
}
