import { IStudent } from 'app/shared/model/student.model';
import { IExamSubject } from 'app/shared/model/exam-subject.model';

export interface IStudentResult {
  id?: number;
  marksObtained?: number | null;
  student?: IStudent | null;
  examSubject?: IExamSubject | null;
}

export const defaultValue: Readonly<IStudentResult> = {};
