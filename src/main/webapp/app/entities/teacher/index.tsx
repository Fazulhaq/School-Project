import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Teacher from './teacher';
import TeacherDetail from './teacher-detail';
import TeacherUpdate from './teacher-update';
import TeacherDeleteDialog from './teacher-delete-dialog';

const TeacherRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Teacher />} />
    <Route path="new" element={<TeacherUpdate />} />
    <Route path=":id">
      <Route index element={<TeacherDetail />} />
      <Route path="edit" element={<TeacherUpdate />} />
      <Route path="delete" element={<TeacherDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TeacherRoutes;
