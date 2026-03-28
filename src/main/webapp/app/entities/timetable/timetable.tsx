import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './timetable.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const Timetable = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const timetableList = useAppSelector(state => state.timetable.entities);
  const loading = useAppSelector(state => state.timetable.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  const breadcrumbItems = [{ label: 'Timetables', url: '' }];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <h2 id="timetable-heading" data-cy="TimetableHeading">
        {/* <Translate contentKey="schoolMisApp.timetable.home.title">Timetables</Translate> */}
        <div className="d-flex justify-content-end">
          <Link to="/timetable/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="schoolMisApp.timetable.home.createLabel">Create new Timetable</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {timetableList && timetableList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="schoolMisApp.timetable.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dayOfWeek')}>
                  <Translate contentKey="schoolMisApp.timetable.dayOfWeek">Day Of Week</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dayOfWeek')} />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="schoolMisApp.timetable.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="schoolMisApp.timetable.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.timetable.section">Section</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.timetable.subject">Subject</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.timetable.teacher">Teacher</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {timetableList.map((timetable, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/timetable/${timetable.id}`} color="link" size="sm">
                      {timetable.id}
                    </Button>
                  </td>
                  <td>{timetable.dayOfWeek}</td>
                  <td>
                    {timetable.startTime ? <TextFormat type="date" value={timetable.startTime} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{timetable.endTime ? <TextFormat type="date" value={timetable.endTime} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{timetable.section ? <Link to={`/section/${timetable.section.id}`}>{timetable.section.id}</Link> : ''}</td>
                  <td>{timetable.subject ? <Link to={`/subject/${timetable.subject.id}`}>{timetable.subject.id}</Link> : ''}</td>
                  <td>{timetable.teacher ? <Link to={`/teacher/${timetable.teacher.id}`}>{timetable.teacher.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/timetable/${timetable.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/timetable/${timetable.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/timetable/${timetable.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="schoolMisApp.timetable.home.notFound">No Timetables found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Timetable;
