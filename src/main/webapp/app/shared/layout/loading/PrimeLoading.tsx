// app/shared/layout/loading/PrimeLoading.tsx
import React from 'react';
import { ProgressSpinner } from 'primereact/progressspinner';

const PrimeLoading = () => {
  return (
    <div className="card">
      <ProgressSpinner style={{ width: '50px', height: '50px' }} strokeWidth="8" fill="var(--surface-ground)" animationDuration=".5s" />
    </div>
  );
};

export default PrimeLoading;