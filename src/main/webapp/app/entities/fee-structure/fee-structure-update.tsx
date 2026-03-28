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
import { IFeeStructure } from 'app/shared/model/fee-structure.model';
import { getEntity, updateEntity, createEntity, reset } from './fee-structure.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const FeeStructureUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentClasses = useAppSelector(state => state.studentClass.entities);
  const feeStructureEntity = useAppSelector(state => state.feeStructure.entity);
  const loading = useAppSelector(state => state.feeStructure.loading);
  const updating = useAppSelector(state => state.feeStructure.updating);
  const updateSuccess = useAppSelector(state => state.feeStructure.updateSuccess);

  const handleClose = () => {
    navigate('/fee-structure');
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }

    const entity = {
      ...feeStructureEntity,
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
        ...feeStructureEntity,
        studentClass: feeStructureEntity?.studentClass?.id,
      };

  const breadcrumbItems = [
    { label: "Fee Structures", url: '/fee-structure' },
    { label: "Create or Update Fee Structure", url: '' },
  ];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="schoolMisApp.feeStructure.home.createOrEditLabel" data-cy="FeeStructureCreateUpdateHeading">
            <Translate contentKey="schoolMisApp.feeStructure.home.createOrEditLabel">Create or edit a FeeStructure</Translate>
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
                  id="fee-structure-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('schoolMisApp.feeStructure.amount')}
                id="fee-structure-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('schoolMisApp.feeStructure.academicYear')}
                id="fee-structure-academicYear"
                name="academicYear"
                data-cy="academicYear"
                type="text"
              />
              <ValidatedField
                id="fee-structure-studentClass"
                name="studentClass"
                data-cy="studentClass"
                label={translate('schoolMisApp.feeStructure.studentClass')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/fee-structure" replace color="info">
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

export default FeeStructureUpdate;
