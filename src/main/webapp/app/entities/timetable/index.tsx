import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Timetable from './timetable';
import TimetableDetail from './timetable-detail';
import TimetableUpdate from './timetable-update';
import TimetableDeleteDialog from './timetable-delete-dialog';

const TimetableRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Timetable />} />
    <Route path="new" element={<TimetableUpdate />} />
    <Route path=":id">
      <Route index element={<TimetableDetail />} />
      <Route path="edit" element={<TimetableUpdate />} />
      <Route path="delete" element={<TimetableDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimetableRoutes;
