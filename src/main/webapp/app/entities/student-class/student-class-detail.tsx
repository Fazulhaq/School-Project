import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-class.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const StudentClassDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentClassEntity = useAppSelector(state => state.studentClass.entity);

  const breadcrumbItems = [
    { label: "Student Classes", url: '/student-class' },
    { label: "View Student Class Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="studentClassDetailsHeading">
          <Translate contentKey="schoolMisApp.studentClass.detail.title">StudentClass</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentClassEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="schoolMisApp.studentClass.name">Name</Translate>
            </span>
          </dt>
          <dd>{studentClassEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/student-class" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-class/${studentClassEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentClassDetail;
