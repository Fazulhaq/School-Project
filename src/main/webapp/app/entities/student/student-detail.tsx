import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);

  const breadcrumbItems = [
    { label: "Students", url: '/student' },
    { label: "View Student Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">
          <Translate contentKey="schoolMisApp.student.detail.title">Student</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="schoolMisApp.student.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="schoolMisApp.student.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="schoolMisApp.student.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {studentEntity.dateOfBirth ? <TextFormat value={studentEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gender">
              <Translate contentKey="schoolMisApp.student.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{studentEntity.gender}</dd>
          <dt>
            <span id="admissionDate">
              <Translate contentKey="schoolMisApp.student.admissionDate">Admission Date</Translate>
            </span>
          </dt>
          <dd>
            {studentEntity.admissionDate ? (
              <TextFormat value={studentEntity.admissionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="schoolMisApp.student.status">Status</Translate>
            </span>
          </dt>
          <dd>{studentEntity.status}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.student.user">User</Translate>
          </dt>
          <dd>{studentEntity.user ? studentEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
