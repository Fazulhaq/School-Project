import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TeacherSubject from './teacher-subject';
import TeacherSubjectDetail from './teacher-subject-detail';
import TeacherSubjectUpdate from './teacher-subject-update';
import TeacherSubjectDeleteDialog from './teacher-subject-delete-dialog';

const TeacherSubjectRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TeacherSubject />} />
    <Route path="new" element={<TeacherSubjectUpdate />} />
    <Route path=":id">
      <Route index element={<TeacherSubjectDetail />} />
      <Route path="edit" element={<TeacherSubjectUpdate />} />
      <Route path="delete" element={<TeacherSubjectDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TeacherSubjectRoutes;
