import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentResult from './student-result';
import StudentResultDetail from './student-result-detail';
import StudentResultUpdate from './student-result-update';
import StudentResultDeleteDialog from './student-result-delete-dialog';

const StudentResultRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentResult />} />
    <Route path="new" element={<StudentResultUpdate />} />
    <Route path=":id">
      <Route index element={<StudentResultDetail />} />
      <Route path="edit" element={<StudentResultUpdate />} />
      <Route path="delete" element={<StudentResultDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentResultRoutes;
