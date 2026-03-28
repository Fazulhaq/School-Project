import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './fee-structure.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const FeeStructureDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feeStructureEntity = useAppSelector(state => state.feeStructure.entity);

  const breadcrumbItems = [
    { label: "Fee Structures", url: '/fee-structure' },
    { label: "View Fee Structure Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="feeStructureDetailsHeading">
          <Translate contentKey="schoolMisApp.feeStructure.detail.title">FeeStructure</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feeStructureEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="schoolMisApp.feeStructure.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{feeStructureEntity.amount}</dd>
          <dt>
            <span id="academicYear">
              <Translate contentKey="schoolMisApp.feeStructure.academicYear">Academic Year</Translate>
            </span>
          </dt>
          <dd>{feeStructureEntity.academicYear}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.feeStructure.studentClass">Student Class</Translate>
          </dt>
          <dd>{feeStructureEntity.studentClass ? feeStructureEntity.studentClass.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/fee-structure" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fee-structure/${feeStructureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeeStructureDetail;
