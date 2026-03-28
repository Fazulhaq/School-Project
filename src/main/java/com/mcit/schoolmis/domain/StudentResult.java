package com.mcit.schoolmis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A StudentResult.
 */
@Entity
@Table(name = "student_result")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "marks_obtained", precision = 21, scale = 2)
    private BigDecimal marksObtained;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "exam", "subject" }, allowSetters = true)
    private ExamSubject examSubject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMarksObtained() {
        return this.marksObtained;
    }

    public StudentResult marksObtained(BigDecimal marksObtained) {
        this.setMarksObtained(marksObtained);
        return this;
    }

    public void setMarksObtained(BigDecimal marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentResult student(Student student) {
        this.setStudent(student);
        return this;
    }

    public ExamSubject getExamSubject() {
        return this.examSubject;
    }

    public void setExamSubject(ExamSubject examSubject) {
        this.examSubject = examSubject;
    }

    public StudentResult examSubject(ExamSubject examSubject) {
        this.setExamSubject(examSubject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentResult)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentResult{" +
            "id=" + getId() +
            ", marksObtained=" + getMarksObtained() +
            "}";
    }
}
