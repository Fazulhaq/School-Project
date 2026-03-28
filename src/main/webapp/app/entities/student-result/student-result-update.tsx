import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { IExamSubject } from 'app/shared/model/exam-subject.model';
import { getEntities as getExamSubjects } from 'app/entities/exam-subject/exam-subject.reducer';
import { IStudentResult } from 'app/shared/model/student-result.model';
import { getEntity, updateEntity, createEntity, reset } from './student-result.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const StudentResultUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const students = useAppSelector(state => state.student.entities);
  const examSubjects = useAppSelector(state => state.examSubject.entities);
  const studentResultEntity = useAppSelector(state => state.studentResult.entity);
  const loading = useAppSelector(state => state.studentResult.loading);
  const updating = useAppSelector(state => state.studentResult.updating);
  const updateSuccess = useAppSelector(state => state.studentResult.updateSuccess);

  const handleClose = () => {
    navigate('/student-result' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudents({}));
    dispatch(getExamSubjects({}));
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
    if (values.marksObtained !== undefined && typeof values.marksObtained !== 'number') {
      values.marksObtained = Number(values.marksObtained);
    }

    const entity = {
      ...studentResultEntity,
      ...values,
      student: students.find(it => it.id.toString() === values.student?.toString()),
      examSubject: examSubjects.find(it => it.id.toString() === values.examSubject?.toString()),
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
        ...studentResultEntity,
        student: studentResultEntity?.student?.id,
        examSubject: studentResultEntity?.examSubject?.id,
      };

  const breadcrumbItems = [
    { label: "Student Results", url: '/student-result' },
    { label: "Create or Edit Student Result", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.studentResult.home.createOrEditLabel" data-cy="StudentResultCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.studentResult.home.createOrEditLabel">Create or edit a StudentResult</Translate>
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
                  id="student-result-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('schoolMisApp.studentResult.marksObtained')}
                id="student-result-marksObtained"
                name="marksObtained"
                data-cy="marksObtained"
                type="text"
              />
              <ValidatedField
                id="student-result-student"
                name="student"
                data-cy="student"
                label={translate('schoolMisApp.studentResult.student')}
                type="select"
              >
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="student-result-examSubject"
                name="examSubject"
                data-cy="examSubject"
                label={translate('schoolMisApp.studentResult.examSubject')}
                type="select"
              >
                <option value="" key="0" />
                {examSubjects
                  ? examSubjects.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student-result" replace color="info">
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

export default StudentResultUpdate;
