import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import FeeStructure from './fee-structure';
import FeeStructureDetail from './fee-structure-detail';
import FeeStructureUpdate from './fee-structure-update';
import FeeStructureDeleteDialog from './fee-structure-delete-dialog';

const FeeStructureRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FeeStructure />} />
    <Route path="new" element={<FeeStructureUpdate />} />
    <Route path=":id">
      <Route index element={<FeeStructureDetail />} />
      <Route path="edit" element={<FeeStructureUpdate />} />
      <Route path="delete" element={<FeeStructureDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeeStructureRoutes;
