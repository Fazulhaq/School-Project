import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './fee-structure.reducer';
import DynamicBreadcrumb from 'app/shared/util/breadCrumb';

export const FeeStructure = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const feeStructureList = useAppSelector(state => state.feeStructure.entities);
  const loading = useAppSelector(state => state.feeStructure.loading);

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

  const breadcrumbItems = [{ label: 'Fee Structures', url: '' }];

  return (
    <div>
      <DynamicBreadcrumb items={breadcrumbItems} />
      <h2 id="fee-structure-heading" data-cy="FeeStructureHeading">
        {/* <Translate contentKey="schoolMisApp.feeStructure.home.title">Fee Structures</Translate> */}
        <div className="d-flex justify-content-end">
          <Link to="/fee-structure/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="schoolMisApp.feeStructure.home.createLabel">Create new Fee Structure</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {feeStructureList && feeStructureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="schoolMisApp.feeStructure.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="schoolMisApp.feeStructure.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('academicYear')}>
                  <Translate contentKey="schoolMisApp.feeStructure.academicYear">Academic Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('academicYear')} />
                </th>
                <th>
                  <Translate contentKey="schoolMisApp.feeStructure.studentClass">Student Class</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {feeStructureList.map((feeStructure, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/fee-structure/${feeStructure.id}`} color="link" size="sm">
                      {feeStructure.id}
                    </Button>
                  </td>
                  <td>{feeStructure.amount}</td>
                  <td>{feeStructure.academicYear}</td>
                  <td>
                    {feeStructure.studentClass ? (
                      <Link to={`/student-class/${feeStructure.studentClass.id}`}>{feeStructure.studentClass.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/fee-structure/${feeStructure.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/fee-structure/${feeStructure.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/fee-structure/${feeStructure.id}/delete`)}
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
              <Translate contentKey="schoolMisApp.feeStructure.home.notFound">No Fee Structures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FeeStructure;
