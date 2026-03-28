import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enrollment.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const EnrollmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const enrollmentEntity = useAppSelector(state => state.enrollment.entity);

  const breadcrumbItems = [
    { label: "Enrollments", url: '/enrollment' },
    { label: "View Enrollment Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="enrollmentDetailsHeading">
          <Translate contentKey="schoolMisApp.enrollment.detail.title">Enrollment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{enrollmentEntity.id}</dd>
          <dt>
            <span id="academicYear">
              <Translate contentKey="schoolMisApp.enrollment.academicYear">Academic Year</Translate>
            </span>
          </dt>
          <dd>{enrollmentEntity.academicYear}</dd>
          <dt>
            <span id="rollNumber">
              <Translate contentKey="schoolMisApp.enrollment.rollNumber">Roll Number</Translate>
            </span>
          </dt>
          <dd>{enrollmentEntity.rollNumber}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.enrollment.student">Student</Translate>
          </dt>
          <dd>{enrollmentEntity.student ? enrollmentEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.enrollment.section">Section</Translate>
          </dt>
          <dd>{enrollmentEntity.section ? enrollmentEntity.section.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/enrollment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enrollment/${enrollmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnrollmentDetail;
