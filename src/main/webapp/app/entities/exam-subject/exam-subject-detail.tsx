import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './exam-subject.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const ExamSubjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const examSubjectEntity = useAppSelector(state => state.examSubject.entity);

   const breadcrumbItems = [
    { label: "Exam Subjects", url: '/exam-subject' },
    { label: "View Exam Subject Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="examSubjectDetailsHeading">
          <Translate contentKey="schoolMisApp.examSubject.detail.title">ExamSubject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examSubjectEntity.id}</dd>
          <dt>
            <span id="maxMarks">
              <Translate contentKey="schoolMisApp.examSubject.maxMarks">Max Marks</Translate>
            </span>
          </dt>
          <dd>{examSubjectEntity.maxMarks}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.examSubject.exam">Exam</Translate>
          </dt>
          <dd>{examSubjectEntity.exam ? examSubjectEntity.exam.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.examSubject.subject">Subject</Translate>
          </dt>
          <dd>{examSubjectEntity.subject ? examSubjectEntity.subject.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/exam-subject" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam-subject/${examSubjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamSubjectDetail;
