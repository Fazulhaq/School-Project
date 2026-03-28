import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './teacher-subject.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TeacherSubjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const teacherSubjectEntity = useAppSelector(state => state.teacherSubject.entity);

  const breadcrumbItems = [
    { label: "Teacher Subjects", url: '/teacher-subject' },
    { label: "View Teacher Subject Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="teacherSubjectDetailsHeading">
          <Translate contentKey="schoolMisApp.teacherSubject.detail.title">TeacherSubject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{teacherSubjectEntity.id}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.teacherSubject.teacher">Teacher</Translate>
          </dt>
          <dd>{teacherSubjectEntity.teacher ? teacherSubjectEntity.teacher.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.teacherSubject.subject">Subject</Translate>
          </dt>
          <dd>{teacherSubjectEntity.subject ? teacherSubjectEntity.subject.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.teacherSubject.section">Section</Translate>
          </dt>
          <dd>{teacherSubjectEntity.section ? teacherSubjectEntity.section.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/teacher-subject" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/teacher-subject/${teacherSubjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TeacherSubjectDetail;
