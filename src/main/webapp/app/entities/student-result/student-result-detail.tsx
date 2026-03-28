import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student-result.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const StudentResultDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentResultEntity = useAppSelector(state => state.studentResult.entity);

   const breadcrumbItems = [
    { label: "Student Results", url: '/student-result' },
    { label: "View Student Result Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="studentResultDetailsHeading">
          <Translate contentKey="schoolMisApp.studentResult.detail.title">StudentResult</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentResultEntity.id}</dd>
          <dt>
            <span id="marksObtained">
              <Translate contentKey="schoolMisApp.studentResult.marksObtained">Marks Obtained</Translate>
            </span>
          </dt>
          <dd>{studentResultEntity.marksObtained}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.studentResult.student">Student</Translate>
          </dt>
          <dd>{studentResultEntity.student ? studentResultEntity.student.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.studentResult.examSubject">Exam Subject</Translate>
          </dt>
          <dd>{studentResultEntity.examSubject ? studentResultEntity.examSubject.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/student-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student-result/${studentResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentResultDetail;
