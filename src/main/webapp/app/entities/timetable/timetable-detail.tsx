import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './timetable.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TimetableDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timetableEntity = useAppSelector(state => state.timetable.entity);

   const breadcrumbItems = [
    { label: "Timetables", url: '/timetable' },
    { label: "View Timetable Details", url: '' },
  ];

  return (
    <Row>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <Col md="8">
        <h2 data-cy="timetableDetailsHeading">
          <Translate contentKey="schoolMisApp.timetable.detail.title">Timetable</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timetableEntity.id}</dd>
          <dt>
            <span id="dayOfWeek">
              <Translate contentKey="schoolMisApp.timetable.dayOfWeek">Day Of Week</Translate>
            </span>
          </dt>
          <dd>{timetableEntity.dayOfWeek}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="schoolMisApp.timetable.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {timetableEntity.startTime ? <TextFormat value={timetableEntity.startTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="schoolMisApp.timetable.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {timetableEntity.endTime ? <TextFormat value={timetableEntity.endTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="schoolMisApp.timetable.section">Section</Translate>
          </dt>
          <dd>{timetableEntity.section ? timetableEntity.section.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.timetable.subject">Subject</Translate>
          </dt>
          <dd>{timetableEntity.subject ? timetableEntity.subject.id : ''}</dd>
          <dt>
            <Translate contentKey="schoolMisApp.timetable.teacher">Teacher</Translate>
          </dt>
          <dd>{timetableEntity.teacher ? timetableEntity.teacher.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/timetable" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/timetable/${timetableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimetableDetail;
