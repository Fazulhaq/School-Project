import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Student from './student';
import Parent from './parent';
import Teacher from './teacher';
import StudentClass from './student-class';
import Section from './section';
import Subject from './subject';
import TeacherSubject from './teacher-subject';
import Enrollment from './enrollment';
import Attendance from './attendance';
import Exam from './exam';
import ExamSubject from './exam-subject';
import StudentResult from './student-result';
import FeeStructure from './fee-structure';
import Payment from './payment';
import Timetable from './timetable';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="student/*" element={<Student />} />
        <Route path="parent/*" element={<Parent />} />
        <Route path="teacher/*" element={<Teacher />} />
        <Route path="student-class/*" element={<StudentClass />} />
        <Route path="section/*" element={<Section />} />
        <Route path="subject/*" element={<Subject />} />
        <Route path="teacher-subject/*" element={<TeacherSubject />} />
        <Route path="enrollment/*" element={<Enrollment />} />
        <Route path="attendance/*" element={<Attendance />} />
        <Route path="exam/*" element={<Exam />} />
        <Route path="exam-subject/*" element={<ExamSubject />} />
        <Route path="student-result/*" element={<StudentResult />} />
        <Route path="fee-structure/*" element={<FeeStructure />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="timetable/*" element={<Timetable />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
