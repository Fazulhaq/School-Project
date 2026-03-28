package com.mcit.schoolmis.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ExamSubject.
 */
@Entity
@Table(name = "exam_subject")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "max_marks")
    private Integer maxMarks;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExamSubject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxMarks() {
        return this.maxMarks;
    }

    public ExamSubject maxMarks(Integer maxMarks) {
        this.setMaxMarks(maxMarks);
        return this;
    }

    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ExamSubject exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ExamSubject subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamSubject)) {
            return false;
        }
        return getId() != null && getId().equals(((ExamSubject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamSubject{" +
            "id=" + getId() +
            ", maxMarks=" + getMaxMarks() +
            "}";
    }
}
