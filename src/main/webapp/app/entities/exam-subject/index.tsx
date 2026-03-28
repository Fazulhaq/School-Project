import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExamSubject from './exam-subject';
import ExamSubjectDetail from './exam-subject-detail';
import ExamSubjectUpdate from './exam-subject-update';
import ExamSubjectDeleteDialog from './exam-subject-delete-dialog';

const ExamSubjectRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExamSubject />} />
    <Route path="new" element={<ExamSubjectUpdate />} />
    <Route path=":id">
      <Route index element={<ExamSubjectDetail />} />
      <Route path="edit" element={<ExamSubjectUpdate />} />
      <Route path="delete" element={<ExamSubjectDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExamSubjectRoutes;
