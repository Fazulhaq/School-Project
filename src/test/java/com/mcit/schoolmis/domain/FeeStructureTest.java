package com.mcit.schoolmis.domain;

import static com.mcit.schoolmis.domain.FeeStructureTestSamples.*;
import static com.mcit.schoolmis.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.schoolmis.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeeStructureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeeStructure.class);
        FeeStructure feeStructure1 = getFeeStructureSample1();
        FeeStructure feeStructure2 = new FeeStructure();
        assertThat(feeStructure1).isNotEqualTo(feeStructure2);

        feeStructure2.setId(feeStructure1.getId());
        assertThat(feeStructure1).isEqualTo(feeStructure2);

        feeStructure2 = getFeeStructureSample2();
        assertThat(feeStructure1).isNotEqualTo(feeStructure2);
    }

    @Test
    void studentClassTest() {
        FeeStructure feeStructure = getFeeStructureRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        feeStructure.setStudentClass(studentClassBack);
        assertThat(feeStructure.getStudentClass()).isEqualTo(studentClassBack);

        feeStructure.studentClass(null);
        assertThat(feeStructure.getStudentClass()).isNull();
    }
}
