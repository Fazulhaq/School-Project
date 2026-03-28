package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClass.class);
        StudentClass studentClass1 = getStudentClassSample1();
        StudentClass studentClass2 = new StudentClass();
        assertThat(studentClass1).isNotEqualTo(studentClass2);

        studentClass2.setId(studentClass1.getId());
        assertThat(studentClass1).isEqualTo(studentClass2);

        studentClass2 = getStudentClassSample2();
        assertThat(studentClass1).isNotEqualTo(studentClass2);
    }
}
