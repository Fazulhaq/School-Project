import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './teacher-subject.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const TeacherSubject = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const teacherSubjectList = useAppSelector(state => state.teacherSubject.entities);
  const loading = useAppSelector(state => state.teacherSubject.loading);

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

  const breadcrumbItems = [{ label: 'Teacher Subjects', url: '' }];
  
  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <h2 id="teacher-subject-heading" data-cy="TeacherSubjectHeading">
        {/* <Translate contentKey="schoolMisApp.teacherSubject.home.title">Teacher Subjects</Translate> */}
        <div className="d-flex justify-content-end">
          <Link to="/teacher-subject/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="schoolMisApp.teacherSubject.home.createLabel">Create new Teacher Subject</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {teacherSubjectList && teacherSubjectList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="schoolMisApp.teacherSubject.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.teacherSubject.teacher">Teacher</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.teacherSubject.subject">Subject</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.teacherSubject.section">Section</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {teacherSubjectList.map((teacherSubject, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/teacher-subject/${teacherSubject.id}`} color="link" size="sm">
                      {teacherSubject.id}
                    </Button>
                  </td>
                  <td>
                    {teacherSubject.teacher ? <Link to={`/teacher/${teacherSubject.teacher.id}`}>{teacherSubject.teacher.id}</Link> : ''}
                  </td>
                  <td>
                    {teacherSubject.subject ? <Link to={`/subject/${teacherSubject.subject.id}`}>{teacherSubject.subject.id}</Link> : ''}
                  </td>
                  <td>
                    {teacherSubject.section ? <Link to={`/section/${teacherSubject.section.id}`}>{teacherSubject.section.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/teacher-subject/${teacherSubject.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/teacher-subject/${teacherSubject.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/teacher-subject/${teacherSubject.id}/delete`)}
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
              <Translate contentKey="schoolMisApp.teacherSubject.home.notFound">No Teacher Subjects found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TeacherSubject;
