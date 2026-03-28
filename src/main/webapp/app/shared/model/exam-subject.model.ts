import { IExam } from 'app/shared/model/exam.model';
import { ISubject } from 'app/shared/model/subject.model';

export interface IExamSubject {
  id?: number;
  maxMarks?: number | null;
  exam?: IExam | null;
  subject?: ISubject | null;
}

export const defaultValue: Readonly<IExamSubject> = {};
