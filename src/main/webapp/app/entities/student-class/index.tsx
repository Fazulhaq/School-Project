import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentClass from './student-class';
import StudentClassDetail from './student-class-detail';
import StudentClassUpdate from './student-class-update';
import StudentClassDeleteDialog from './student-class-delete-dialog';

const StudentClassRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentClass />} />
    <Route path="new" element={<StudentClassUpdate />} />
    <Route path=":id">
      <Route index element={<StudentClassDetail />} />
      <Route path="edit" element={<StudentClassUpdate />} />
      <Route path="delete" element={<StudentClassDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentClassRoutes;
