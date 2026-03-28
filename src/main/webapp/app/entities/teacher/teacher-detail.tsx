import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './teacher.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TeacherDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const teacherEntity = useAppSelector(state => state.teacher.entity);

   const breadcrumbItems = [
    { label: "Teachers", url: '/teacher' },
    { label: "View Teacher Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="teacherDetailsHeading">
          <Translate contentKey="schoolMisApp.teacher.detail.title">Teacher</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="schoolMisApp.teacher.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="schoolMisApp.teacher.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.lastName}</dd>
          <dt>
            <span id="hireDate">
              <Translate contentKey="schoolMisApp.teacher.hireDate">Hire Date</Translate>
            </span>
          </dt>
          <dd>
            {teacherEntity.hireDate ? <TextFormat value={teacherEntity.hireDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="qualification">
              <Translate contentKey="schoolMisApp.teacher.qualification">Qualification</Translate>
            </span>
          </dt>
          <dd>{teacherEntity.qualification}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.teacher.user">User</Translate>
          </dt>
          <dd>{teacherEntity.user ? teacherEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/teacher" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/teacher/${teacherEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TeacherDetail;
