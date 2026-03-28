import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITeacher } from 'app/shared/model/teacher.model';
import { getEntities as getTeachers } from 'app/entities/teacher/teacher.reducer';
import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { ISection } from 'app/shared/model/section.model';
import { getEntities as getSections } from 'app/entities/section/section.reducer';
import { ITeacherSubject } from 'app/shared/model/teacher-subject.model';
import { getEntity, updateEntity, createEntity, reset } from './teacher-subject.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TeacherSubjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const teachers = useAppSelector(state => state.teacher.entities);
  const subjects = useAppSelector(state => state.subject.entities);
  const sections = useAppSelector(state => state.section.entities);
  const teacherSubjectEntity = useAppSelector(state => state.teacherSubject.entity);
  const loading = useAppSelector(state => state.teacherSubject.loading);
  const updating = useAppSelector(state => state.teacherSubject.updating);
  const updateSuccess = useAppSelector(state => state.teacherSubject.updateSuccess);

  const handleClose = () => {
    navigate('/teacher-subject');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTeachers({}));
    dispatch(getSubjects({}));
    dispatch(getSections({}));
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

    const entity = {
      ...teacherSubjectEntity,
      ...values,
      teacher: teachers.find(it => it.id.toString() === values.teacher?.toString()),
      subject: subjects.find(it => it.id.toString() === values.subject?.toString()),
      section: sections.find(it => it.id.toString() === values.section?.toString()),
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
        ...teacherSubjectEntity,
        teacher: teacherSubjectEntity?.teacher?.id,
        subject: teacherSubjectEntity?.subject?.id,
        section: teacherSubjectEntity?.section?.id,
      };

  const breadcrumbItems = [
    { label: "Teacher Subjects", url: '/teacher-subject' },
    { label: "Create or Edit Teacher Subject", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.teacherSubject.home.createOrEditLabel" data-cy="TeacherSubjectCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.teacherSubject.home.createOrEditLabel">Create or edit a TeacherSubject</Translate>
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
                  id="teacher-subject-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="teacher-subject-teacher"
                name="teacher"
                data-cy="teacher"
                label={translate('schoolMisApp.teacherSubject.teacher')}
                type="select"
              >
                <option value="" key="0" />
                {teachers
                  ? teachers.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="teacher-subject-subject"
                name="subject"
                data-cy="subject"
                label={translate('schoolMisApp.teacherSubject.subject')}
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
              <ValidatedField
                id="teacher-subject-section"
                name="section"
                data-cy="section"
                label={translate('schoolMisApp.teacherSubject.section')}
                type="select"
              >
                <option value="" key="0" />
                {sections
                  ? sections.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/teacher-subject" replace color="info">
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

export default TeacherSubjectUpdate;
