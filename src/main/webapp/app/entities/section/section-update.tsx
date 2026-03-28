import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStudentClass } from 'app/shared/model/student-class.model';
import { getEntities as getStudentClasses } from 'app/entities/student-class/student-class.reducer';
import { ISection } from 'app/shared/model/section.model';
import { getEntity, updateEntity, createEntity, reset } from './section.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const SectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentClasses = useAppSelector(state => state.studentClass.entities);
  const sectionEntity = useAppSelector(state => state.section.entity);
  const loading = useAppSelector(state => state.section.loading);
  const updating = useAppSelector(state => state.section.updating);
  const updateSuccess = useAppSelector(state => state.section.updateSuccess);

  const handleClose = () => {
    navigate('/section');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudentClasses({}));
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
      ...sectionEntity,
      ...values,
      studentClass: studentClasses.find(it => it.id.toString() === values.studentClass?.toString()),
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
        ...sectionEntity,
        studentClass: sectionEntity?.studentClass?.id,
      };

  const breadcrumbItems = [
    { label: "Sections", url: '/section' },
    { label: "Create or Edit Section", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.section.home.createOrEditLabel" data-cy="SectionCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.section.home.createOrEditLabel">Create or edit a Section</Translate>
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
                  id="section-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('schoolMisApp.section.name')}
                id="section-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="section-studentClass"
                name="studentClass"
                data-cy="studentClass"
                label={translate('schoolMisApp.section.studentClass')}
                type="select"
              >
                <option value="" key="0" />
                {studentClasses
                  ? studentClasses.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.id}
                    </option>
                  ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/section" replace color="info">
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

export default SectionUpdate;
