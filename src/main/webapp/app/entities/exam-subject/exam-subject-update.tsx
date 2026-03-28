import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExam } from 'app/shared/model/exam.model';
import { getEntities as getExams } from 'app/entities/exam/exam.reducer';
import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { IExamSubject } from 'app/shared/model/exam-subject.model';
import { getEntity, updateEntity, createEntity, reset } from './exam-subject.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const ExamSubjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const exams = useAppSelector(state => state.exam.entities);
  const subjects = useAppSelector(state => state.subject.entities);
  const examSubjectEntity = useAppSelector(state => state.examSubject.entity);
  const loading = useAppSelector(state => state.examSubject.loading);
  const updating = useAppSelector(state => state.examSubject.updating);
  const updateSuccess = useAppSelector(state => state.examSubject.updateSuccess);

  const handleClose = () => {
    navigate('/exam-subject');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getExams({}));
    dispatch(getSubjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.maxMarks !== undefined && typeof values.maxMarks !== 'number') {
      values.maxMarks = Number(values.maxMarks);
    }

    const entity = {
      ...examSubjectEntity,
      ...values,
      exam: exams.find(it => it.id.toString() === values.exam?.toString()),
      subject: subjects.find(it => it.id.toString() === values.subject?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
        ...examSubjectEntity,
        exam: examSubjectEntity?.exam?.id,
        subject: examSubjectEntity?.subject?.id,
      };

  const breadcrumbItems = [
    { label: "Exam Subjects", url: '/exam-subject' },
    { label: "Create or Update Exam Subject", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.examSubject.home.createOrEditLabel" data-cy="ExamSubjectCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.examSubject.home.createOrEditLabel">Create or edit a ExamSubject</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="exam-subject-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('schoolMisApp.examSubject.maxMarks')}
                id="exam-subject-maxMarks"
                name="maxMarks"
                data-cy="maxMarks"
                type="text"
              />
              <ValidatedField
                id="exam-subject-exam"
                name="exam"
                data-cy="exam"
                label={translate('schoolMisApp.examSubject.exam')}
                type="select"
              >
                <option value="" key="0" />
                {exams
                  ? exams.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="exam-subject-subject"
                name="subject"
                data-cy="subject"
                label={translate('schoolMisApp.examSubject.subject')}
                type="select"
              >
                <option value="" key="0" />
                {subjects
                  ? subjects.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/exam-subject" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExamSubjectUpdate;
