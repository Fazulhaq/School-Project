import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISection } from 'app/shared/model/section.model';
import { getEntities as getSections } from 'app/entities/section/section.reducer';
import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { ITeacher } from 'app/shared/model/teacher.model';
import { getEntities as getTeachers } from 'app/entities/teacher/teacher.reducer';
import { ITimetable } from 'app/shared/model/timetable.model';
import { getEntity, updateEntity, createEntity, reset } from './timetable.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TimetableUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sections = useAppSelector(state => state.section.entities);
  const subjects = useAppSelector(state => state.subject.entities);
  const teachers = useAppSelector(state => state.teacher.entities);
  const timetableEntity = useAppSelector(state => state.timetable.entity);
  const loading = useAppSelector(state => state.timetable.loading);
  const updating = useAppSelector(state => state.timetable.updating);
  const updateSuccess = useAppSelector(state => state.timetable.updateSuccess);

  const handleClose = () => {
    navigate('/timetable');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSections({}));
    dispatch(getSubjects({}));
    dispatch(getTeachers({}));
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
      ...timetableEntity,
      ...values,
      section: sections.find(it => it.id.toString() === values.section?.toString()),
      subject: subjects.find(it => it.id.toString() === values.subject?.toString()),
      teacher: teachers.find(it => it.id.toString() === values.teacher?.toString()),
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
        ...timetableEntity,
        section: timetableEntity?.section?.id,
        subject: timetableEntity?.subject?.id,
        teacher: timetableEntity?.teacher?.id,
      };

  const breadcrumbItems = [
    { label: "Timetables", url: '/timetable' },
    { label: "Create or Edit Timetable", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.timetable.home.createOrEditLabel" data-cy="TimetableCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.timetable.home.createOrEditLabel">Create or edit a Timetable</Translate>
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
                  id="timetable-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('schoolMisApp.timetable.dayOfWeek')}
                id="timetable-dayOfWeek"
                name="dayOfWeek"
                data-cy="dayOfWeek"
                type="text"
              />
              <ValidatedField
                label={translate('schoolMisApp.timetable.startTime')}
                id="timetable-startTime"
                name="startTime"
                data-cy="startTime"
                type="date"
              />
              <ValidatedField
                label={translate('schoolMisApp.timetable.endTime')}
                id="timetable-endTime"
                name="endTime"
                data-cy="endTime"
                type="date"
              />
              <ValidatedField
                id="timetable-section"
                name="section"
                data-cy="section"
                label={translate('schoolMisApp.timetable.section')}
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
              <ValidatedField
                id="timetable-subject"
                name="subject"
                data-cy="subject"
                label={translate('schoolMisApp.timetable.subject')}
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
                id="timetable-teacher"
                name="teacher"
                data-cy="teacher"
                label={translate('schoolMisApp.timetable.teacher')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/timetable" replace color="info">
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

export default TimetableUpdate;
