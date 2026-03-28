import student from 'app/entities/student/student.reducer';
import parent from 'app/entities/parent/parent.reducer';
import teacher from 'app/entities/teacher/teacher.reducer';
import studentClass from 'app/entities/student-class/student-class.reducer';
import section from 'app/entities/section/section.reducer';
import subject from 'app/entities/subject/subject.reducer';
import teacherSubject from 'app/entities/teacher-subject/teacher-subject.reducer';
import enrollment from 'app/entities/enrollment/enrollment.reducer';
import attendance from 'app/entities/attendance/attendance.reducer';
import exam from 'app/entities/exam/exam.reducer';
import examSubject from 'app/entities/exam-subject/exam-subject.reducer';
import studentResult from 'app/entities/student-result/student-result.reducer';
import feeStructure from 'app/entities/fee-structure/fee-structure.reducer';
import payment from 'app/entities/payment/payment.reducer';
import timetable from 'app/entities/timetable/timetable.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  student,
  parent,
  teacher,
  studentClass,
  section,
  subject,
  teacherSubject,
  enrollment,
  attendance,
  exam,
  examSubject,
  studentResult,
  feeStructure,
  payment,
  timetable,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
