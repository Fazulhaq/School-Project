import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './exam.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const ExamDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const examEntity = useAppSelector(state => state.exam.entity);

   const breadcrumbItems = [
    { label: "Exams", url: '/exam' },
    { label: "View Exam Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="examDetailsHeading">
          <Translate contentKey="schoolMisApp.exam.detail.title">Exam</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{examEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="schoolMisApp.exam.name">Name</Translate>
            </span>
          </dt>
          <dd>{examEntity.name}</dd>
          <dt>
            <span id="academicYear">
              <Translate contentKey="schoolMisApp.exam.academicYear">Academic Year</Translate>
            </span>
          </dt>
          <dd>{examEntity.academicYear}</dd>
        </dl>
        <Button tag={Link} to="/exam" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/exam/${examEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExamDetail;
